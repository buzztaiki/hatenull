package buzztaiki.hatenull;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class Processor extends AbstractProcessor {
    private @Nullable Context context;

    @Override
    public void init(@Nonnull ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        if (!(processingEnv instanceof JavacProcessingEnvironment)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "HateNull requires javac v1.6 or greater.");
            return;
        }
        context = ((JavacProcessingEnvironment)processingEnv).getContext();
    }

    @Override
    public boolean process (@Nonnull Set<? extends TypeElement> annotations, @Nonnull RoundEnvironment roundEnv) {
        if (context == null) return false;

        for (Element elem : roundEnv.getRootElements()) {
            final JCTree.JCCompilationUnit unit = toUnit(elem);
            if (unit == null) continue;
            if (unit.sourcefile.getKind() != JavaFileObject.Kind.SOURCE) continue;

            unit.accept(new TreeTranslator() {
                @Override public void visitMethodDef(JCTree.JCMethodDecl tree) {
                    super.visitMethodDef(tree);
                    tree.mods.annotations = addNonnull(tree.mods.annotations);
                    for (JCTree.JCVariableDecl var : tree.params) {
                        var.mods.annotations = addNonnull(tree.mods.annotations);
                    }
                }
                @Override public void visitVarDef(JCTree.JCVariableDecl tree) {
                    super.visitVarDef(tree);
                    List<JCTree> path = TreeInfo.pathFor(tree, unit);
                    if (path.size() > 1 && path.get(1).getTag() == JCTree.CLASSDEF) {
                        tree.mods.annotations = addNonnull(tree.mods.annotations);
                    }
                }
            });
        }
        return true;
    }

    private @Nonnull List<JCTree.JCAnnotation> addNonnull(@Nonnull List<JCTree.JCAnnotation> annotations) {
        for (JCTree.JCAnnotation annot : annotations) {
            JCTree type = annot.annotationType;
            String name;
            if (type instanceof JCTree.JCIdent) {
                JCTree.JCIdent ident = (JCTree.JCIdent)type;
                name = ident.sym.toString();
            } else {
                name = type.toString();
            }
            if (name.equals("javax.annotation.Nonnull")) return annotations;
            if (name.equals("javax.annotation.Nullable")) return annotations;
            if (name.equals("javax.annotation.CheckForNull")) return annotations;
        }
        TreeMaker treeMaker = TreeMaker.instance(context);
        return annotations.prepend(
            treeMaker.Annotation(chainDots("javax", "annotation", "Nonnull"), List.<JCTree.JCExpression>nil()));
    }

    private @Nonnull JCTree.JCExpression chainDots(String ... names) {
        TreeMaker treeMaker = TreeMaker.instance(context);
        JavacElements elements = JavacElements.instance(context);
        JCTree.JCExpression e = treeMaker.Ident(elements.getName(names[0]));
        for (int i = 1; i < names.length; i++) {
            e = treeMaker.Select(e, elements.getName(names[i]));
        }
        return e;
    }

    private @Nullable JCTree.JCCompilationUnit toUnit(@Nonnull Element element) {
        TreePath path = Trees.instance(processingEnv).getPath(element);
        return (path == null) ? null : (JCTree.JCCompilationUnit)path.getCompilationUnit();
    }
}

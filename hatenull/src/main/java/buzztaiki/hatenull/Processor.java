package buzztaiki.hatenull;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
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
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class Processor extends AbstractProcessor {
    private @Nullable Trees trees;

    @Override
    public void init(@Nonnull ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        if (!(processingEnv instanceof JavacProcessingEnvironment)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "HateNull requires javac v1.6 or greater.");
            return;
        }
        trees = Trees.instance(processingEnv);
    }

    @Override
    public boolean process (@Nonnull Set<? extends TypeElement> annotations, @Nonnull RoundEnvironment roundEnv) {
        if (trees == null) return false;

        for (Element elem : roundEnv.getRootElements()) {
            JCTree.JCCompilationUnit unit = toUnit(elem);
            if (unit == null) continue;

            if (unit.sourcefile.getKind() != JavaFileObject.Kind.SOURCE) {
                continue;
            }

            unit.accept(new TreeTranslator() {
                @Override public void visitMethodDef(JCTree.JCMethodDecl tree) {
                    super.visitMethodDef(tree);
                    tree.mods.annotations = addNonnull(tree.mods.annotations);
                    for (JCTree.JCVariableDecl var : tree.params) {
                        var.mods.annotations = addNonnull(tree.mods.annotations);
                    }
                }
            });
        }
        return true;
    }

    private @Nonnull List<JCTree.JCAnnotation> addNonnull(@Nonnull List<JCTree.JCAnnotation> annotations) {
        for (JCTree.JCAnnotation annot : annotations) {
        }
        return annotations;
    }

    private @Nullable JCTree.JCCompilationUnit toUnit(@Nonnull Element element) {
        TreePath path = trees.getPath(element);
        return (path == null) ? null : (JCTree.JCCompilationUnit)path.getCompilationUnit();
    }
}

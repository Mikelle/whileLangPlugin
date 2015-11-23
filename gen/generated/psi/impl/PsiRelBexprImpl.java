// This is a generated file. Not intended for manual editing.
package generated.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.intellij.whileLang.psi.WhileTypes.*;
import com.intellij.whileLang.psi.impl.*;

public class PsiRelBexprImpl extends PsiBexprImpl implements PsiRelBexpr {

  public PsiRelBexprImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) ((PsiVisitor)visitor).visitRelBexpr(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiExpr.class);
  }

  @Override
  @NotNull
  public PsiRel getRel() {
    return findNotNullChildByClass(PsiRel.class);
  }

  @Override
  @NotNull
  public PsiExpr getLeft() {
    List<PsiExpr> p1 = getExprList();
    return p1.get(0);
  }

  @Override
  @Nullable
  public PsiExpr getRight() {
    List<PsiExpr> p1 = getExprList();
    return p1.size() < 2 ? null : p1.get(1);
  }

}

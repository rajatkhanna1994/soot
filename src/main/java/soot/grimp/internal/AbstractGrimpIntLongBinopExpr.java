/* Soot - a J*va Optimization Framework
 * Copyright (C) 1999 Patrick Lam
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/*
 * Modified by the Sable Research Group and others 1997-1999.
 * See the 'credits' file distributed with Soot for the complete list of
 * contributors.  (Soot is distributed at http://www.sable.mcgill.ca/soot)
 */


package soot.grimp.internal;

import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.jimple.DivExpr;
import soot.jimple.SubExpr;
import soot.jimple.internal.AbstractIntLongBinopExpr;

abstract public class AbstractGrimpIntLongBinopExpr
    extends AbstractIntLongBinopExpr implements Precedence {
  AbstractGrimpIntLongBinopExpr(Value op1, Value op2) {
    this(Grimp.v().newArgBox(op1),
        Grimp.v().newArgBox(op2));
  }

  protected AbstractGrimpIntLongBinopExpr(ValueBox op1Box, ValueBox op2Box) {
    this.op1Box = op1Box;
    this.op2Box = op2Box;
  }

  abstract public int getPrecedence();

  private String toString(Value op1, Value op2,
                          String leftOp, String rightOp) {
    if (op1 instanceof Precedence &&
        ((Precedence) op1).getPrecedence() < getPrecedence()) {
      leftOp = "(" + leftOp + ")";
    }

    if (op2 instanceof Precedence) {
      int opPrec = ((Precedence) op2).getPrecedence();
      int myPrec = getPrecedence();

      if ((opPrec < myPrec) ||
          ((opPrec == myPrec) && ((this instanceof SubExpr) || (this instanceof DivExpr)))) {
        rightOp = "(" + rightOp + ")";
      }
    }

    return leftOp + getSymbol() + rightOp;
  }

  public String toString() {
    Value op1 = op1Box.getValue();
    Value op2 = op2Box.getValue();
    String leftOp = op1.toString();
    String rightOp = op2.toString();

    return toString(op1, op2, leftOp, rightOp);
  }
}

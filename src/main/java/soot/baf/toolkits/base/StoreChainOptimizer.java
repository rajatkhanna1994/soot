package soot.baf.toolkits.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.Unit;
import soot.baf.PushInst;
import soot.baf.StoreInst;
import soot.toolkits.scalar.Pair;

/**
 * Due to local packing, we may have chains of assignments to the same local.
 * <p>
 * push null;
 * store.r $r2;
 * <p>
 * push null;
 * store.r $r2;
 * <p>
 * This transformer eliminates the redundant push/store instructions.
 *
 * @author Steven Arzt
 */
public class StoreChainOptimizer extends BodyTransformer {

  public StoreChainOptimizer(Singletons.Global g) {
  }

  public static StoreChainOptimizer v() {
    return G.v().soot_baf_toolkits_base_StoreChainOptimizer();
  }

  @Override
  protected void internalTransform(Body b, String phaseName,
                                   Map<String, String> options) {
    // We keep track of all the stored values
    Map<Local, Pair<Unit, Unit>> stores = new HashMap<Local, Pair<Unit, Unit>>();
    Set<Unit> toRemove = new HashSet<Unit>();

    Unit lastPush = null;
    for (Unit u : b.getUnits()) {
      // If we can jump here from somewhere, do not modify this code
      if (!u.getBoxesPointingToThis().isEmpty()) {
        stores.clear();
        lastPush = null;
      } else if (u instanceof PushInst) {
        // Emulate pushing stuff on the stack
        lastPush = u;
      } else if (u instanceof StoreInst && lastPush != null) {
        StoreInst si = (StoreInst) u;
        Pair<Unit, Unit> pushStorePair = stores.get(si.getLocal());
        if (pushStorePair != null) {
          // We can remove the push and the store
          toRemove.add(pushStorePair.getO1());
          toRemove.add(pushStorePair.getO2());
        }

        stores.put(si.getLocal(), new Pair<Unit, Unit>(lastPush, u));
      } else {
        // We're outside of the trivial initialization chain
        stores.clear();
        lastPush = null;
      }
    }

    b.getUnits().removeAll(toRemove);
  }

}

/* Soot - a J*va Optimization Framework
 * Copyright (C) 1999-2010 Hossein Sadat-Mohtasham
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

package soot.toolkits.graph.pdg;


import java.util.List;

import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.MutableEdgeLabelledDirectedGraph;


/**
 * This defines the interface to a Program Dependence Graph as defined in
 * <p>
 * Ferrante, J., Ottenstein, K. J., and Warren, J. D. 1987.
 * The program dependence graph and its use in optimization.
 * ACM Trans. Program. Lang. Syst. 9, 3 (Jul. 1987), 319-349.
 * DOI= http://doi.acm.org/10.1145/24039.24041
 * <p>
 * Note that this interface should evolve based on the need.
 *
 * @author Hossein Sadat-Mohtasham
 * May 2009
 */

public interface ProgramDependenceGraph extends MutableEdgeLabelledDirectedGraph<PDGNode, String> {

  /**
   * @return A List of weak regions, generated by RegionAnalysis for the corresponding
   * control flow graph (These are Regions and not PDGRegions.)
   */
  public List<Region> getWeakRegions();

  /**
   * @return A List of strong regions, generated when constructing the program dependence graph
   * (These are Regions and not PDGRegions.)
   */
  public List<Region> getStrongRegions();

  /**
   * This method returns the list of PDGRegions computed by the construction method.
   *
   * @return The list of PDGRegions
   */
  public List<PDGRegion> getPDGRegions();

  /**
   * @return The root region of the PDG.
   */
  public IRegion GetStartRegion();

  public BlockGraph getBlockGraph();

  /**
   * @return The root node of the PDG, which is essentially the same as the start region
   * but packaged in its PDGNode, which can be used to traverse the graph, etc.
   */
  public PDGNode GetStartNode();

  /**
   * This method determines if node1 is control-dependent on node2 in this PDG.
   *
   * @param node1
   * @param node2
   * @return returns true if node1 is dependent on node2
   */
  public boolean dependentOn(PDGNode node1, PDGNode node2);

  /**
   * This method returns the list of all dependents of a node in the PDG.
   *
   * @param node is the PDG node whose dependents are desired.
   * @return a list of dependent nodes
   */
  public List<PDGNode> getDependents(PDGNode node);

  /**
   * This method returns the PDGNode in the PDG corresponding to the given
   * CFG node. Note that currently the CFG node has to be a Block.
   *
   * @param cfgNode is expected to be a node in CFG (currently only Block).
   * @return The node in PDG corresponding to cfgNode.
   */

  public PDGNode getPDGNode(Object cfgNode);

  /**
   * @return A human readable description of the PDG.
   */
  public String toString();

}

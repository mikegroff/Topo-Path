//Michael Groff
// COP 3503, Fall 2017
//2779827

import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

class Vertex
{
  //didnt want to make an edge class so the vertex class holds
  // an identifier, a walk int to keep track of whether weve already attempted to visit all of its neighbors
  // and lists of edges to and from this vertex
  int ident;
  int walked;
  ArrayList<Integer> edgeTo;
  ArrayList<Integer> edgeFrom;

  Vertex(int postion)
  {
    this.ident = postion;
    this.walked = 0;
    this.edgeTo = new ArrayList<Integer>();
    this.edgeFrom = new ArrayList<Integer>();
  }
}

public class TopoPath
{
  //no constructor for this program so we can assumed everything is gonna be static
  //also since only one method is called in the testcases well need to generate the graph
  //every time were check for a path
  public static int order; //number of vertices
  public static ArrayList<Integer> path; //TopoPath
  public static HashMap<Integer, Boolean> viable; //vertices with no edges pointing to them
  public static HashMap<Integer, Vertex> graph; // hashmap holding all the vertices, essentially an adjacnecy list
  public static HashMap<Integer, Boolean> used; // vertices already visited in the path, faster than searching path

  public static void scanFile(String filename) throws FileNotFoundException
  {
      viable = new HashMap<Integer, Boolean>();
      graph = new HashMap<Integer, Vertex>();
      used = new HashMap<Integer, Boolean>();
      //we scan everything into the graph in O(n) time
      //works a bit differenctly but the first int in ever line determines what to scan
      //into each vertex

      Scanner s = new Scanner(new File(filename));
      int N = s.nextInt();
      order = N;
      Vertex zero = new Vertex(0);//decided to make a zero vertex that will hold all possible starting points
      for(int i=1; i <= N ; i++)
      {
        Vertex V = new Vertex(i);
        graph.put(i,V);
        used.put(i,false);//nothing has been used yet
        viable.put(i,true);
      }
      for(int k = 1; k <=N; k++)
      {
        int P = s.nextInt();
        for(int j=0; j < P ; j++)
        {
          int edge = s.nextInt();
          graph.get(k).edgeTo.add(edge);
          graph.get(edge).edgeFrom.add(k);
          viable.put(edge, false);
        }
      }
      for(int l = 1; l <= N; l++)
      {
        if(viable.get(l) == true)
          zero.edgeTo.add(l);
      }
      graph.put(0,zero);
  }

  public static boolean hasTopoPath(String filename)  throws FileNotFoundException
  {
    scanFile(filename);
    path = new ArrayList<Integer>();
    Vertex at = graph.get(0);//starting here since this is where all viable options are
    path.add(0);
    int opt = 0, optMax = at.edgeTo.size(), pathSize = 0;
      //if weve tried to go down every path from the zero vertex and failed
      //then a TopoPath DNE, we use opt to keep track of this
    while(opt < optMax)
    {
      //if we've tried every neighbor its tiem to backtrack
      if(at.walked >= at.edgeTo.size())
      {
        if(at.ident == 0)
        {
          opt++;
          continue;
        }
        //this part is the backtracking
        //removes from path and sets used to false
        //sets the examining vertex to the last vertex in the path
        at.walked = 0;
        used.put(at.ident, false);
        path.remove(path.size()-1);
        at = graph.get(path.get(path.size()-1));
        continue;
      }

      //if there are more to check we do so using a loop
      //here the walked variable keeps track of where we last we chekcing before traveling down a path
      Vertex check = graph.get(at.edgeTo.get(at.walked));
      boolean cont = true;

      for(int i = 0; i < check.edgeFrom.size(); i++)
        cont = cont && used.get(check.edgeFrom.get(i));
      //if one of the neighbors is valid to travel for a TopoPath we do so
      if(cont == true)
      {
        at.walked++;
        at = check;
        path.add(check.ident);
        used.put(at.ident, true);
        if(path.size() > order)
          return true;//with vertex zero our path is gonna need to be bigger than the order
      }
      else
        at.walked++;
      //we repeat the backtracking part again
      if(at.walked >= at.edgeTo.size())
      {
        at.walked = 0;
        used.put(at.ident, false);
        path.remove(path.size()-1);
        at = graph.get(path.get(path.size()-1));
      }
      //if we make it this far and we are still on vertex zero
      //we mark down the path from it and check the next one
      if(at.ident == 0)
          opt++;
    }
    return false;
  }

  public static double difficultyRating()
  {
  return 2.5;//
  }

  public static double hoursSpent()
  {
  return 4.5;//
  }

}

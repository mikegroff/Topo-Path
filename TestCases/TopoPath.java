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
  public static int order;
  public static ArrayList<Integer> path;
  public static HashMap<Integer, Boolean> viable;
  public static HashMap<Integer, Vertex> graph;
  public static HashMap<Integer, Boolean> used;

  public static void scanFile(String filename) throws FileNotFoundException
  {
      viable = new HashMap<Integer, Boolean>();
      graph = new HashMap<Integer, Vertex>();
      used = new HashMap<Integer, Boolean>();

      Scanner s = new Scanner(new File(filename));
      int N = s.nextInt();
      order = N;
      Vertex zero = new Vertex(0);
      for(int i=1; i <= N ; i++)
      {
        Vertex V = new Vertex(i);
        graph.put(i,V);
        used.put(i,false);
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
    Vertex at = graph.get(0);
    path.add(0);
    int opt = 0, optMax = at.edgeTo.size(), pathSize = 0;
    while(opt < optMax)
    {
      Vertex check = graph.get(at.edgeTo.get(at.walked));
      boolean cont = true;
      for(int i = 0; i < check.edgeFrom.size(); i++)
        cont = cont && used.get(check.edgeFrom.get(i));
      if(cont == true)
      {
        at = check;
        path.add(check.ident);
        used.put(at.ident, true);
        if(path.size() >= order)
          return true;
      }
      else
        at.walked++;

      if(at.walked >= at.edgeTo.size())
      {
        at.walked = 0;
        used.put(at.ident, false);
        path.remove(path.size()-1);
        at = graph.get(path.get(path.size()-1));
      }

        if(at.ident == 0)
          opt++;
    }
    return false;
  }

  public static double difficultyRating()
  {
  return 1.0;//
  }

  public static double hoursSpent()
  {
  return 1.5;//
  }

}

import java.util.*;

class Parser
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter all the Terminals,Comma separated(NO spaces)");        
        String term[]=sc.nextLine().split(",");
        Grammar g = new Grammar();
        g.addTerminal(term);
        System.out.println("Enter all the Terminals,Comma separated(NO spaces)");        
        String nterm[]=sc.nextLine().split(",");
        g.addNonTerminal(nterm);
        System.out.println("Enter Rules(press -1 to break)(L->R)(enter . to separate 2 symbols)");
        while(true)
        {
            String r=sc.nextLine();
            //System.out.println("-"+r+"-");
            if(r.equals("-1"))
                break;
            g.addProduction(r+"|");
        }
        g.display();
        HashMap<Symbol,HashSet<Symbol>> first = g.getFirst();
        for(Symbol ss : first.keySet() )
        {
            System.out.print("\nFirst("+ss.name+"): ");
            for(Symbol ele : first.get(ss))
            {
                System.out.print(ele.name+",");
            }
        }

    }
}

class Symbol
{
    final String name;
    final boolean is_term;
    Symbol(String s,boolean val)
    {
        name=s;
        is_term=val;
    }
    @Override
    public boolean equals(Object o)
    {
        Symbol oo = (Symbol)o;
        if(this.name==oo.name && this.is_term==oo.is_term)
        {
            return true;
        }
        return false;
    }
    @Override    
    public int hashCode()
    {
        return this.name.hashCode();
    }

}
class Production
{
    Symbol left;
    ArrayList<ArrayList<Symbol>> right;
    Production()
    {
        left=null;
        right=new ArrayList<ArrayList<Symbol>>();
    }
}
class Grammar
{
    ArrayList<Symbol> Terminal;
    ArrayList<Symbol> NonTerminal;
    Symbol start;
    ArrayList<Production> rules;
    //HashMap<Symbol,>
    Grammar()
    {
        Terminal = new ArrayList<Symbol>();
        NonTerminal = new ArrayList<Symbol>();
        rules=new ArrayList<Production>();
    }

    public HashMap<Symbol,HashSet<Symbol>> getFirst()
    {
        HashMap<Symbol,HashSet<Symbol>> first = new HashMap<Symbol,HashSet<Symbol>>();
        for(Symbol t : NonTerminal)
        {
            HashSet<Symbol> hs = new HashSet<Symbol>();
            Stack<Symbol> rec = new Stack<Symbol>();
            rec.push(t);
            while(!rec.empty())
            {
            	Symbol p= rec.pop();
            	ArrayList<ArrayList<Symbol>> rl = getRules(p);//correct till here
                for(ArrayList<Symbol> ors :rl)
                {
                    System.out.println(ors.get(0).name+ors.get(0).is_term);
                    if((ors.get(0)).is_term==true)
                    {
                        System.out.print("Adding ot HS:"+ors.get(0).name);
                        boolean b=hs.add(ors.get(0));
                        System.out.println(b);
                    }
                    else
                    {
                        rec.push(ors.get(0));
                    }
                }
            }
            first.put(t,hs);
        }
        return first;
    }
    public ArrayList<ArrayList<Symbol>> getRules(Symbol t)
    {
        for(Production p : rules)
        {
            if(p.left.name==t.name)
            {
                //System.out.print("getRules match:"+t.name);
                return p.right;
            }
        }
        return new ArrayList<ArrayList<Symbol>>();
    }
    public void addTerminal(String arr[])
    {
        for(String i:arr)
        {
            Symbol t = new Symbol(i,true);
            Terminal.add(t);
        }
    }
    public void addNonTerminal(String arr[])
    {
        for(String i:arr)
        {
            Symbol t = new Symbol(i,false);
            NonTerminal.add(t);
        }
    }
    public void addProduction(String s)
    {
        String lr[] = s.split("->");
        Production p = new Production();
        int lindex=myIndex(NonTerminal,lr[0]);
        //System.out.print(lr[0]);
        System.out.print("lr[1]:"+lr[1]);
        if(lindex==-1)
        {System.out.println("Not valid Terminal.not Added in Production list");return;}
        else {
            boolean flag=false;
            for(Production x : rules)
            {
                if((x.left).equals(NonTerminal.get(lindex)))
                {p=x;flag=true;break;}
            }
            if(flag==false){
                //p=new Production();
                p.left=NonTerminal.get(lindex);
            }
        }
        String ors[]=lr[1].split("\\|");
        System.out.print("ors[0]:"+ors[0]);
        for(String subrule : ors)
        {
            ArrayList<Symbol> alpha = new ArrayList<Symbol>();
            String[] syms= subrule.split("\\.");
            System.out.print("-syms[0]"+syms[0]);
            for(String sym : syms)
            {
                System.out.print(sym+"*");
                int rindex=myIndex(Terminal,sym);
                if(rindex==-1)
                {
                    rindex=myIndex(NonTerminal,sym);
                
                    if(rindex==-1)
                    {
                        System.out.println("Not valud symbol . not changed");break;
                    }
                    
                    else{
                        System.out.println("Getting added:"+NonTerminal.get(rindex).name);
                        alpha.add(NonTerminal.get(rindex));
                    }
                }
                else{
                    System.out.println("Getting added:"+Terminal.get(rindex).name);
                    alpha.add(Terminal.get(rindex));
                }
            }
            p.right.add(alpha);
        }
        rules.add(p);
    }
    public int myIndex(ArrayList<Symbol> list, String sym)
    {
        int idx=-1,ctr=-1;
        for(Symbol elem : list)
        {
            ctr++;
            if(sym.equals(elem.name))
            {
                idx=ctr;break;
            }
        }
        return idx;
    }
    public void display()
    {
        System.out.println("Printing Terminals");
        for(Symbol s:Terminal)
        {
            System.out.print(s.name+"_");
        }
        System.out.println("\nPrinting Terminals");
        for(Symbol s:NonTerminal)
        {
            System.out.print(s.name+"_");
        }
        System.out.println("\nPrinting Production Rules");
        for(Production p:rules)
        {
            System.out.print("\n"+p.left.name+"->");
            for(ArrayList<Symbol> i : p.right)
            {
                //System.out.print(i.size());
                for(Symbol sk :i)
                {
                    System.out.print(sk.name+"_");
                }
                System.out.print("|");
            }
        }
        System.out.println("_______________\n");
    }

}

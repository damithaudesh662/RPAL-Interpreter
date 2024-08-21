package CSEMachine;

import java.util.ArrayList;
import java.util.HashMap;

public class Symbol {
    protected String data;
    
    public Symbol(String data) {
        this.data = data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public String getData() {
        return this.data;
    }
}

 class B extends Symbol {
    public ArrayList<Symbol> symbols;

    public B() {
        super("b");
    }
}

class Beta extends Symbol {
    public Beta() {
        super("beta");
    }
}

class Bool extends Rand {
    public Bool(String data) {
        super(data);
    }
}

class Bop extends Rator{
    public Bop(String data) {
        super(data);
    }
}

class Delta extends Symbol {
    private int index;
    public ArrayList<Symbol> symbols;

    public Delta(int i) {
        super("delta");
        this.setIndex(i);
    }

    private void setIndex(int i) {
        this.index = i;
    }

    public int getIndex() {
        return this.index;
    }
}

class Dummy extends Rand {
    public Dummy() {
        super("dummy");
    }
}

class Rator extends Symbol {
    public Rator(String data) {
        super(data);
    }
}

class Uop extends Rator {
    public Uop(String data) {
        super(data);
    }
}

class Ystar extends Symbol {
    public Ystar() {
        super("<Y*>");
    }
}

class Lambda extends Symbol {
    private int index;
    private int environment;
    public ArrayList<Id> identifiers;
    private Delta delta;

    public Lambda(int i) {
        super("lambda");
        this.setIndex(i);
        this.identifiers = new ArrayList<Id>();
    }

    private void setIndex(int i) {
        this.index = i;
    }

    public int getIndex() {
        return this.index;
    }

    public void setEnvironment(int n) {
        this.environment = n;
    }

    public int getEnvironment() {
        return this.environment;
    }

    public void setDelta(Delta delta) {
        this.delta = delta;
    }

    public Delta getDelta() {
        return this.delta;
    }
}

class Rand extends Symbol {
    public Rand(String data) {
        super(data);
    }

    @Override
    public String getData() {
        return super.getData();
    }
}

class Str extends Rand {
    public Str(String data) {
        super(data);
    }
}

class Tau extends Symbol {
    private int n;

    public Tau(int n) {
        super("tau");
        this.setN(n);
    }

    private void setN(int n) {
        this.n = n;
    }

    public int getN() {
        return this.n;
    }
}

class Tup extends Rand {
    public ArrayList<Symbol> symbols;

    public Tup() {
        super("tup");
        this.symbols = new ArrayList<Symbol>();
    }
}

class Int extends Rand {
    public Int(String data) {
        super(data);
    }
}

class Id extends Rand {
    public Id(String data) {
        super(data);
    }

    @Override
    public String getData() {
        return super.getData();
    }
}

class Gamma extends Symbol {
    public Gamma() {
        super("gamma");
    }
}

class Eta extends Symbol {
    private int index;
    private int environment;
    private Id identifier;
    private Lambda lambda;

    public Eta() {
        super("eta");
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public int getIndex() {
        return this.index;
    }

    public void setEnvironment(int e) {
        this.environment = e;
    }

    public int getEnvironment() {
        return this.environment;
    }

    public void setIdentifier(Id id) {
        this.identifier = id;
    }

    public void setLambda(Lambda lambda) {
        this.lambda = lambda;
    }

    public Lambda getLambda() {
        return this.lambda;
    }

}

class Err extends Symbol {
    public Err() {
        super("error");
    }
}

class E extends Symbol {
    private int index;
    private E parent;
    private boolean isRemoved = false;
    public HashMap<Id,Symbol> values;

    public E(int i) {
        super("e");
        this.setIndex(i);
        this.values = new HashMap<Id,Symbol>();
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public int getIndex() {
        return this.index;
    }

    public void setParent(E e) {
        this.parent = e;
    }

    public E getParent() {
        return this.parent;
    }

    public void setIsRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }

    public boolean getIsRemoved() {
        return this.isRemoved;
    }

    public Symbol lookup(Id id){
        for (Id key: this.values.keySet()) {
            if (key.getData().equals(id.getData())){
                return this.values.get(key);
            }
        }
        if (this.parent != null) {
            return this.parent.lookup(id);
        } else {
            return new Symbol(id.getData());
        }
    }
}
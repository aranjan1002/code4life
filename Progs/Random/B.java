class B {
    A a = new A();
     public String get() {
       return toString();
     }
  public String toString() {
    return a.toString();
  }

public static void main(String[] args) {
    System.out.println(new B().get());
}
}

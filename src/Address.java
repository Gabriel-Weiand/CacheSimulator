public class Address {
    String tag;
    String index;
    String offset;
    public Address(String tag, String indice, String offset) {
        this.tag = tag;
        this.index = indice;
        this.offset = offset;
    }
    public void PrintAddress() {
        System.out.println("Tag: " + tag+" Index: " + index+" Offset: " + offset);
    }
}

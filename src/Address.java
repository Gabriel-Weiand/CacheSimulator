public class Address {
    private String tag;
    private String index;
    private String offset;
    public Address(String tag, String indice, String offset) {
        this.tag = tag;
        this.index = indice;
        this.offset = offset;
    }

    public String getTag() {
        return tag;
    }

    public String getIndex() {
        return index;
    }

    public String getOffset() {
        return offset;
    }

    public void PrintAddress() {
        System.out.println("Tag: " + tag+" Index: " + index+" Offset: " + offset);
    }
}

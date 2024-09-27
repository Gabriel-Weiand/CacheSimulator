import java.util.LinkedList;

public class LineIndex {

    private LinkedList<TagVal> tagVals;
    public LineIndex() {
        tagVals = new LinkedList<>();
    }
    public void setTagVals(LinkedList<TagVal> tagVals) {
        this.tagVals = tagVals;
    }
    public void addTagVal(TagVal tagVal,int index) {
        tagVals.set(index,tagVal);
    }
    public TagVal getTagVal(int index) {
        return tagVals.get(index);
    }
    public int linkedListSize() {
        return tagVals.size();
    }
    public LinkedList<TagVal> getTagValLL() {
        return tagVals;
    }
}

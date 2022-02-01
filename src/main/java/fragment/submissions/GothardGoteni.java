package fragment.submissions;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


//Solution Class : GothardGoteni
public class GothardGoteni{


    private static boolean mergeComplete=false;




    public static void main(String[] args) throws IOException {

        try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
            in.lines()
                    .map(GothardGoteni::reassemble)
                    .forEach(System.out::println);
        }

    }




    /*********
     *
     *  reassemble Fragment
     *
     *
     * @author  Gothard GOTENI
     * @version 1.0
     * @since   2022-01-31
     *
     *
     * @param input String
     * @return Set<String>
     */
    public static String  reassemble(String input){

        if (input==null || input.isEmpty()) { // Null input is returned as an Empty list
            return "";
        }


        Set<String>  inputSet= new LinkedHashSet<> (Arrays.asList(input.split(";"))); // keep Order and no Duplicate. Also very fast :: LinkedHashSet add-O(1) contains-O(1)  next-O(1)

        Set<String> ret=inputSet;
        while(!mergeComplete) {     // as long as the merge is not completed, we keep asking for mergeFragment (keep Fragmenting)
            ret = mergeFragment(ret); // go and try to merge Fragment
        }

        mergeComplete=false; // we re-init mergeComplete to allow more reassemble

        String value= ret.stream()    // convert set to String
                .collect(Collectors.joining(""));   // the No solution case will be concatenated as well as empty

        return value;

    }







    /*********
     *
     *  mergeFragment Fragment
     *
     *
     * @author  Gothard GOTENI
     * @version 1.0
     * @since   2022-01-31
     *
     *
     * @param inputSet Set<String>
     * @return Set<String>
     */
    private  static Set<String> mergeFragment(Set<String> inputSet){

        Set<String> outPutData = new LinkedHashSet<>();   // keep Order and no Duplicate. Also very fast :: LinkedHashSet add-O(1) contains-O(1)  next-O(1)
        Set<String> inputData = new LinkedHashSet<>(inputSet); // keep Order and no Duplicate. Also very fast :: LinkedHashSet add-O(1) contains-O(1)  next-O(1)
        inputData.removeAll(Collections.singleton(""));   // we are not interested into empty String. So we remove them

        // Define required variable
        boolean isMergeFound=false;
        String firstFragment=""; // Fragment element 1
        String secondFragment=""; // Fragment element 2
        MatchedFragment matchedFragment = null; // Merged Fragment. it should be a merged element from Fragment element 1 +  2
        MatchedFragment vMatchedFragment=null; // used as internal variable to compare

        Iterator<String> firstFragmentIterator = inputData.iterator();  //Iterator and for-each loop are faster than simple for loop for collections with no random access


        //1st Iteration
        while (firstFragmentIterator.hasNext() && !isMergeFound ){ // We iterate over  a first Fragment Element until we find something

            firstFragment=firstFragmentIterator.next();

            Iterator<String> secondFragmentIterator = inputData.iterator(); //Iterator and for-each loop are faster than simple for loop for collections with no random access
            secondFragmentIterator.next(); // the second element should point to another element but the 1st element

            // 2nd Iteration
            while (secondFragmentIterator.hasNext() ){ // We iterate over  a second Fragment Element until we find something that could be matched with the first Fragment Element

                secondFragment= secondFragmentIterator.next();// the second element should point to another element but the 1st element

                if(secondFragment.equals(firstFragment)){
                    break; // We are not interested to compute when both Iterators are pointing to the same value/element
                }

                vMatchedFragment=doMatchAndMerge(firstFragment,secondFragment,true);  // we try to merge in forward and backward direction

                if(vMatchedFragment!=null){   // We have a Merge !
                    matchedFragment=getMaxOverlapMatchedFragment(matchedFragment,vMatchedFragment); // keep the Fragment with the Best Overlap
                    isMergeFound=true;
                }


            }
        }

        if(isMergeFound) {

            inputData.remove(matchedFragment.getElement1()); // we remove element that was used for the Merge
            inputData.remove(matchedFragment.getElement2());  // we remove element that was used for the Merge
            outPutData.add(matchedFragment.getMergeElement()); // Feed output with the Fragment holding the highest overlapping length

        }else{
            mergeComplete=true;   // if we have not found any match, then merge is completed
        }

        outPutData.addAll(inputData);
        if(outPutData.size()==1){   // if the Output size is 1, then merge is completed
            mergeComplete=true;
        }


        return outPutData;

    }


    /*******
     *
     *
     *  getMaxOverlapMatchedFragment     *
     *
     * @author  Gothard GOTENI
     * @version 1.0
     * @since   2022-01-31
     *
     * @param frag1 MatchedFragment
     * @param frag2 MatchedFragment
     * @return MatchedFragment
     */
    private static MatchedFragment getMaxOverlapMatchedFragment(MatchedFragment frag1, MatchedFragment frag2){

        if (frag1==null) {
            return frag2;
        }else if (frag2==null){
            return frag1;
        }

        if(frag1.getOverlapLength() > frag2.getOverlapLength()){  // return the best Frag overlap
            return frag1;
        }else if (frag1.getOverlapLength() < frag2.getOverlapLength()){ // return the best Frag overlap
            return frag2;
        }else {
            return frag1;
        }

    }





    /**
     *
     *  doMatchAndMerge
     *
     *
     * @author  Gothard GOTENI
     * @version 1.0
     * @since   2022-01-31
     *
     *
     *
     * @param element1 String
     * @param element2 String
     * @return MatchedFragment
     */
    private static MatchedFragment  doMatchAndMerge(String element1, String element2, boolean checkBothDirection){

        // Initialise the variable
        MatchedFragment matchedFragment=null; // needed to store the Matched Fragment details
        boolean isMergeFound=false; // state the Merge
        int index= element2.length(); //  index used to compute the overlap

        String overlap=element2; // arbitrary : start the overlap as element2. value will be then completed and calculated as we iterate

        if(element1.contains(element2)){  // if we have Full overlap, then we have a Merge
            matchedFragment = new MatchedFragment(element1,element2,element1,element2.length() );
            return matchedFragment;  // we return the matched Fragment with overlap score
        }


        while(!isMergeFound) {

            overlap =overlap.substring(0,index);

            if(overlap.isEmpty() || overlap.trim().length()==1 ) { // empty overlap or 1 overlap length is out of scope
                break;
            }

            //log.info("[...LOOKING FOR (substr-ed at index 0,"+index+") val ]"+overlap +"[ IN ]"+element1);
            int ret = element1.indexOf(overlap); // is the overlap an indexOf element1 (do we have an overlap at this index) ?

            if(ret!=-1 && element1.endsWith(overlap) && !overlap.equals(element1)) { // Merge require an indexOF (overlap) + overlap needs to be at the end of the String
                isMergeFound = true;
            }else{
                isMergeFound = false;
            }

            if (isMergeFound) { // for readability, we can leave this code here

                String mergedData = element1.substring(0,ret)+element2; // Merge data : element1 + element2 at the right index (computed from indexOf)
                matchedFragment = new MatchedFragment(element1,element2,mergedData,overlap.length() );
                break;
            }

            index--; // reduce the index and keep looking if we have not found anything
        }

        if (matchedFragment==null && checkBothDirection) {
            matchedFragment=doMatchAndMerge(element2,  element1, false); // we change the order to check the order direction
        }


        return matchedFragment;
    }

} // End of the class







// Class to store all Matched Element
class MatchedFragment{

    private final String element1;
    private final String element2;
    private final String mergeElement;
    private final int overlapLength;

    public MatchedFragment(String element1, String element2, String mergeElement, int overlapLength) {
        this.element1 = element1;
        this.element2 = element2;
        this.mergeElement = mergeElement;
        this.overlapLength = overlapLength;
    }

    public String getElement1() {
        return element1;
    }

    public String getElement2() {
        return element2;
    }

    public String getMergeElement() {
        return mergeElement;
    }

    public int getOverlapLength() {
        return overlapLength;
    }

    @Override
    public String toString() {
        return "MatchedData{" +
                "element1='" + element1 + '\'' +
                ", element2='" + element2 + '\'' +
                ", mergeElement='" + mergeElement + '\'' +
                ", overlapLength=" + overlapLength +
                '}';
    }

}

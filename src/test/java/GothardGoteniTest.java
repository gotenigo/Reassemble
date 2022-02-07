import fragment.submissions.*;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;




public class GothardGoteniTest {


    @Test
    public void TestSimpleData() throws IOException {

        System.out.println("________________ TestSimpleData");
        String[] path = new String[]{"src/test/java/sample1.txt"};
        GothardGoteni.main(path);

    }

    @Test
    public void stringtest(){

        System.out.println("________________ stringtest");

        String ret;


        ret= GothardGoteni.reassemble("abccd;ccddcc;ccee");
        assertEquals("abccddccee",ret);

    }




    @Test
    public void TestSimpleData2() throws IOException {

        System.out.println("________________ TestSimpleData2");

        String ret;

        ret= GothardGoteni.reassemble("ABCDEF;DEFG");
        assertEquals("ABCDEFG",ret);

        ret= GothardGoteni.reassemble("ABCDEF;XCDEZ");
        assertEquals("ABCDEFXCDEZ",ret);

        ret= GothardGoteni.reassemble("ABCDEF;XYZABC");
        assertEquals("XYZABCDEF",ret);

        ret= GothardGoteni.reassemble("ABCDEF;BCDE");
        assertEquals("ABCDEF",ret);


    }



    @Test
    public void TestSimpleData3() throws IOException {

        System.out.println("________________ TestSimpleData3");

        String ret;

        ret= GothardGoteni.reassemble("O draconia;conian devil! Oh la;h lame sa;saint!");
        System.out.println("ret="+ret);
        assertEquals(ret.toString(),"O draconian devil! Oh lame saint!");



    }



    @Test
    public void TestSimpleData4() throws IOException {

        System.out.println("________________ TestSimpleData4");

        String ret;

        ret= GothardGoteni.reassemble("m quaerat voluptatem.;pora incidunt ut labore et d;, consectetur, adipisci velit;olore magnam aliqua;idunt ut labore et dolore magn;uptatem.;i dolorem ipsum qu;iquam quaerat vol;psum quia dolor sit amet, consectetur, a;ia dolor sit amet, conse;squam est, qui do;Neque porro quisquam est, qu;aerat voluptatem.;m eius modi tem;Neque porro qui;, sed quia non numquam ei;lorem ipsum quia dolor sit amet;ctetur, adipisci velit, sed quia non numq;unt ut labore et dolore magnam aliquam qu;dipisci velit, sed quia non numqua;us modi tempora incid;Neque porro quisquam est, qui dolorem i;uam eius modi tem;pora inc;am al");
        System.out.println("ret="+ret);
        assertEquals(ret.toString(),"Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.");



    }







}

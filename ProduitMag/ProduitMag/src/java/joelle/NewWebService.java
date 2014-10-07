/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package joelle;

import com.sun.xml.ws.util.StringUtils;
import java.io.StringWriter;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.Convert;
import javax.persistence.EntityManager;
import net.cofares.Magazin;
import net.cofares.Produit;
import net.cofares.control.ProduitController;
import net.cofares.sb.MagazinFacade;
import org.apache.jasper.tagplugins.jstl.ForEach;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author JOELLE
 */
@WebService(serviceName = "NewWebService")
public class NewWebService {

    /**
     * This is a sample web service operation
     *
     * @param txt
     * @return
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        Element root = new Element("RESULT");
        try {

            Element eltName = new Element("NAME");
            eltName.setText(txt);
            root.addContent(eltName);
            root.setAttribute("STATUS", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            root.setAttribute("STATUS", "ERROR");
            root.setText(e.getMessage());
        }
        return new XMLOutputter().outputString(root);

    }

    @WebMethod(operationName = "sum")
    public String sum(@WebParam(name = "nb1") String nb1, @WebParam(name = "nb2") String nb2) {
        Element root = new Element("RESULT");
        try {

            Element eltName = new Element("SUM");
            Integer n1 = new Integer(nb1);
            Integer n2 = new Integer(nb2);

            eltName.setText(String.valueOf(n1 + n2));
            root.addContent(eltName);
            root.setAttribute("STATUS", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            root.setAttribute("STATUS", "ERROR");
            root.setText(e.getMessage());
        }
        return new XMLOutputter().outputString(root);

    }

    @WebMethod(operationName = "list")
    public String list(@WebParam(name = "type") String type) {
        Element root = new Element("RESULT");
        String res = "1";
        try {
            Element eltName = new Element(type);
            //if (type == "Produit") {

            EntityManager em = null;
            em.getEntityManagerFactory();
            MagazinFacade mf = new MagazinFacade();

            List<Magazin> list_m = mf.findAll();

            for (Magazin each : list_m) {
                String id = each.getIdMagazin().toString();
                String nom = each.getNom();

                eltName.setText(id);
            }

            root.addContent(eltName);
            root.setAttribute("STATUS", "OK");
        } catch (Exception e) {
            //throw e;
            //res = e.getMessage();
            e.printStackTrace();
            root.setAttribute("STATUS", "ERROR");
            root.setText(e.getMessage());
        }
        return res;
    }

}

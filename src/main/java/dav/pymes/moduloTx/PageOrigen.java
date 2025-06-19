package dav.pymes.moduloTx;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
import dav.pymes.PagePortalPymes;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
import dxc.util.DXCUtil;

public class PageOrigen extends PagePortalPymes {

	By locPasoPage = By.xpath("//td[@class='TituloRojo']/b[text()='Origen']");
	By locTitTabOr = By.xpath("//td[@class='TituloRojo'][text()='Producto Origen de los Fondos:']");
	
	String xPathLnkServicio = "//a[text()='NB_SERVICIO']";
	String xPathRBOrigen    = "//td[contains(text(),'NUM_PROD')]//preceding-sibling::td[contains(text(),'TIPO_PROD')]//preceding-sibling::td//input";
	String xPathRBOrigenFic = "//td[contains(text(),'NUM_PROD')]//preceding-sibling::td//input";
	String xPathServicio    = "//select[@id='cphCuerpo_dropEmpresaRecaudo']";
	By locTipoPruebaFic     = By.xpath("./parent::td/following-sibling::td[1]");
	By locButtonSig         = By.xpath("//input[contains(@value, 'Siguiente')]");
	
	By seleempresa = By.id("dropMasterEmpresa");
	
	String[] arrTipoProdNoFic = { "AHORRO", "CORRIENTE", "DIPLUS", "EXPRES" };
	
//=======================================================================================================================	
	public PageOrigen(PageLoginPymes parentPage) {
		super(parentPage);
	}
//***********************************************************************************************************************
	/**
	 * Retorna [null] si se pudo ir al link del servicio, en caso contrario retorna un mensaje de error.
	 */
	public String irAServicio(String servicio) throws Exception {
		String nbLink = this.getNbLinkServicio(servicio);
		WebElement objLinkServ = this.element(xPathLnkServicio.replace("NB_SERVICIO", nbLink));
		if (objLinkServ == null) {
//			Evidence.save("ErrorNoServicio", this);
			Evidence.save("ErrorNoServicio");
			return "No se encuentra el link para el servicio [" + servicio + "] -- Se buscá [" + nbLink + "]";
		}
		// DA CLICK EN EL LINK DEL SERVICIO
		this.click(objLinkServ);
		return null;
	}
//***********************************************************************************************************************
	/**
	 * Se presenta la tabla con los productos que pueden ser origen de los fondos.<br>
	 * Retorna [null] si se pudo hacer la selecci�n, en caso contrario retorna un mensaje de error, si pudo hacer la
	 * selecci�n da click en el "Continuar". 
	 */
	public String seleccionarOrigen(String tipoProducto, String numeroProducto) throws Exception {
		WebElement elementPaso;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCI�N DEL ORIGEN
			elementPaso = this.element(locPasoPage);
		} while (elementPaso == null);
//-----------------------------------------------------------------------------------------------------------------------		
		String tipoProdUpper = tipoProducto.toUpperCase();
		String tipoProd = "corriente"; // VALOR POR DEFECTO
		if (tipoProdUpper.contains("AHORRO"))
			tipoProd = "ahorro";
		else if (tipoProdUpper.contains("DIPLUS")) // CR�DIPLUS
			tipoProd = "diplus";
		else if (tipoProdUpper.contains("EXPRES")) // CREDIEXPRESS
			tipoProd = "rediexpress";
		
		WebElement objRadioButtonProd = null;
		String xPathRB = xPathRBOrigen.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4)).replace("TIPO_PROD", tipoProd);
		if (!tipoProdUpper.contains("FIC")) // NO ES FIC
			objRadioButtonProd = this.element(xPathRB);
		
		else { // ES FIC, PUEDE SER CUALQUIER TIPO DE PRODUCTO <> AHORROS, CORRIENTE, CR�DIPLUS, CREDIEXPRESS
			xPathRB = xPathRBOrigenFic.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4));
			String tipoProdTab;
			List<WebElement> listaObjs = this.findElements(By.xpath(xPathRB));
			for (WebElement element : listaObjs) {
				tipoProdTab = this.getText(this.element(element, locTipoPruebaFic));
				if (!DXCUtil.itemContainsAnyArrayItem(tipoProdTab.toUpperCase(), arrTipoProdNoFic)) {
					objRadioButtonProd = element;
					break; // PARA TERMINAR EL CICLO
				}
			}
		}
//-----------------------------------------------------------------------------------------------------------------------
		if (objRadioButtonProd == null) {
			Evidence.saveFullPage("Error-ProdOrigen", this);
			Reporter.write("*** DXC xPath : [" + xPathRB + "]");
			return "Producto origen [" + tipoProducto + " - " + numeroProducto + "] NO encontrado";
		}
		// SI LLEGA A ESTE ES PORQUE EXISTE EL PRODUCTO ORIGEN DE LOS FONDOS
		this.click(objRadioButtonProd);
		Evidence.saveFullPage("ProductoOrigen", this);
		this.click(locButtonSig);
		return null;
	}
//***********************************************************************************************************************
	public String seleccionarOrigen(String numConvenio, String tipoProducto, String numeroProducto) throws Exception {
		
		WebElement elementPaso;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCI�N DEL ORIGEN
			elementPaso = this.element(locPasoPage);
		} while (elementPaso == null);
//-----------------------------------------------------------------------------------------------------------------------		
	    String selecc = "- " + numConvenio; // EL CONVENIO SE MUESTRA CON UN PREFIJO "- "
	    String msgError = this.selectListContainsItems(By.xpath(xPathServicio), selecc);
		if (!msgError.isEmpty())
			return "Error en lista 'Servicio a pagar' : " + msgError;
	    
		WebElement objTablaOrigenes;
		do { // ESPERA MIENTRAS NO SE PRESENTA LA TABLA DE OR�GENES
			objTablaOrigenes = this.element(locTitTabOr);
		} while (objTablaOrigenes == null);
		return this.seleccionarOrigen(tipoProducto, numeroProducto);
	}
//***********************************************************************************************************************
	/**
	 * Retorna el nombre del link del sevicio tal como se presenta en el Portal.<br>
	 * Si el [servicio] no es uno de los que se esperan, retorna "NO HAY LINK DISPONIBLE".
	 */
	private String getNbLinkServicio(String servicio) {
		
		String serv = DXCUtil.removeAccents(servicio).toUpperCase();
		String nbServicio = "NO HAY LINK DISPONIBLE";
		if (serv.contains("NOMI"))
			nbServicio = "Nómina";
		else if (serv.contains("PROV"))
			nbServicio = "Proveedores";
		else if (serv.contains("AFC"))
			nbServicio = "Cuentas AFC";
		else if (serv.contains("SERV") || serv.contains("FACT"))
			nbServicio = "Pago de factura";
		else if (serv.contains("PROV"))
			nbServicio = "Proveedores";
		else if (serv.contains("MISMO"))
			nbServicio = "Entre productos Davivienda del mismo NIT";
		else if (serv.contains("NO INSCR"))
			nbServicio = "Hacia un tercero con cuenta NO inscrita"; 
		else if (serv.contains("INSCR"))
			nbServicio = "Hacia un tercero con cuenta inscrita"; 
		else if (serv.contains("DAVI"))
			nbServicio = "Productos Davivienda";
		else if (serv.contains("AVANCE"))
			nbServicio = "Avances Tarjeta de Crédito";
		else if (serv.contains("TERCEROS") || serv.contains("CRÉDITO"))
			nbServicio = "Créditos de terceros";
		else if (serv.contains("ADMINISTRACION DEL EFECTIVO"))
			nbServicio = "Administración del Efectivo";
		return nbServicio;
	}
	
}

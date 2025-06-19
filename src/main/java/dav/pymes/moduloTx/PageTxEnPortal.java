package dav.pymes.moduloTx;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
import dav.pymes.PagePortalPymes;
import dxc.library.reporting.Evidence;
//import dxc.execution.Evidence;
import dxc.util.DXCUtil;

public class PageTxEnPortal extends PagePortalPymes {

	// CONSTANTES QUE IDENTIFICAN LOS DIFEREMTES ESTADOS DE UNA TRANSACCI�N
	public final static String EST_DECLINADA  = "Transacci�n Declinada";
	public final static String EST_PAGADO     = "Pagado";
	public final static String EST_PAGO_DONE  = "Pago Realizado";
	public final static String EST_PAGO_SUCC  = "Pago Exitoso";
	public final static String EST_TRF_DONE   = "Transferencia Realizada";
	public final static String EST_RET_PROGR  = "Programado"; // PARA RETIROS SIN TARJETA
	public final static String EST_PAGO_PARC  = "Pagado Parcial";
	public final static String EST_PEND_RTA   = "Pendiente Respuesta de Otros Bancos";
	public final static String EST_PEND_RTA1  = "Pendiente de Respuesta"; // Estado que se muestra en portal EMPRESARIAL
	public final static String EST_RECHAZADO  = "Rechazado";
	public final static String EST_PAGO_RECH  = "Pago Rechazado";
	public final static String EST_TRF_RECH   = "Transferencia Rechazada";
	public final static String EST_PEND_APROB = "Pendiente de Aprobar Siguiente Apoderado";
	public final static String EST_PEND_APRO1 = "Pendiente de Actualizar Siguiente Apoderado";
	public final static String EST_PEND_EXEC  = "Pendiente de Ejecuci�n";

	// ARREGLO CON LOS ESTADOS DE UNA TRANSACCI�N QUE PUEDE SER TOMADA PARA INGRESARLA AL MOTOR DE RIESGO
	public final static String[] ARR_ESTADOS_FOR_MR = { EST_PAGADO, EST_PAGO_DONE, EST_PAGO_SUCC, EST_TRF_DONE, EST_RET_PROGR,
			EST_PAGO_PARC, EST_PEND_RTA, EST_PEND_RTA1, EST_PEND_EXEC };
	// ARREGLO CON LOS ESTADOS DE UNA TRANSACCI�N QUE INDICAN QUE FUE REALIZADA
//-----------------------------------------------------------------------------------------------------------------------
	// XPATH PARA LOS LOCATORS QUE IDENTIFICAN EL LINK DE UNA TRANSACCI�N Y SU L�NEA DE DATOS
	String xPathLinkTx       = "//TYPE[text()='NUM_DOC']";
	String xPathDataBeforeTx = "//TYPE[text()='NUM_DOC']//parent::td//preceding-sibling::td";
	String xPathDataAfterTx  = "//TYPE[text()='NUM_DOC']//parent::td//following-sibling::td";
	String xPathListaTxs     = "//td[contains(text(),'FECHA')]"; // [dd/mm/yyyy ]
	
	By locFilaTx    = By.xpath("./parent::td/parent::tr"); // PARA TOMAR EVIDENCIA
	By locSiguiente = By.xpath("//a[@id='cphCuerpo_lnkSiguiente' and @href]");
	By locNumDocDet = By.xpath("//span[@id='cphCuerpo_lblERDocumento' or @id='cphCuerpo_lblMasivoDocumento' or "
			                        + "@id='cphCuerpo_lblPaseDocumento' or @id='cphCuerpo_lblDocumentoPAGP']");

	boolean isLinkNumDoc;
//=======================================================================================================================
	/**
	 * Constructor a partir del [BasePageWeb] padre.
	 */
	public PageTxEnPortal(PageLoginPymes parentPage) {
		super(parentPage);
	}
//***********************************************************************************************************************
	/**
	 * Este m�todo revisa en el listado de Transacciones realizadas en el Portal PYME, para retornar los datos de la
	 * transacci�n cuyo n�mero de documento corresponde al que se recibe por par�metro.<br>
	 * Si la transacci�n NO existe el retorno es [null], si existe se extrae la informaci�n presentada en un array de
	 * String, las posiciones corresponden seg�n se presente en la p�gina Web, ignorando el dato del n�mero del
	 * documento: 0-Fecha, 1-Estado, 2-TipoTx, 3-Tipodestino, 4-Titular, 5-Valor
	 */
	public String[] getDatosTx(String numDocumento) throws Exception {
		
		WebElement linkTx = this.getElementNumDoc(numDocumento);
		if (linkTx == null)
			return null;
		
		String type = "span";
		if (this.isLinkNumDoc) type = "a";
		
		// EN ESTE PUNTO SE ENCONTR� LA TRANSACCI�N
		this.moveToElement(linkTx);
//		Evidence.save("DatosTx" + numDocumento, this);
		Evidence.save("DatosTx" + numDocumento);
		//Evidence.saveFullElement("DatosTx" + numDocumento, this, this.element(linkTx, locFilaTx), false, false);
		
		List<String> listaDatos = new ArrayList<String>();
		// 1- EXTRAE LOS DATOS QUE EST�N ANTES DEL LINK
		List<WebElement> listaObjs = this.findElements(
				By.xpath(xPathDataBeforeTx.replace("TYPE", type).replace("NUM_DOC", numDocumento)));
		for (WebElement obj : listaObjs)
			listaDatos.add(this.getText(obj));
		// 2- EXTRAE LOS DATOS QUE EST�N DESPU�S DEL LINK
		listaObjs = this.findElements(By.xpath(xPathDataAfterTx.replace("TYPE", type).replace("NUM_DOC", numDocumento)));
		for (WebElement obj : listaObjs)
			listaDatos.add(this.getText(obj));
		
		return DXCUtil.listToArray(listaDatos);
	}
//***********************************************************************************************************************
	/**
	 * Permite ingresar al detalle de una transacci�n.<br>
	 * Si la transacci�n NO existe retorna el mensaje respectivo, si existe retorna [null] y deja la respectiva evidencia
	 * de la consulta del detalle.
	 */
	public String entrarDetalleTx(String numDocumento) throws Exception {
		
		WebElement linkTx = this.getElementNumDoc(numDocumento);
		if (linkTx == null)
			return "No se encuentra la transacci�n [" + numDocumento + "]";
		else if (!this.isLinkNumDoc)
			return "No se puede entrar al detalle de la transacci�n [" + numDocumento + "] porque no es un link";
		// EN ESTE PUNTO SE ENCONTR� LA TRANSACCI�N Y ES UN LINK
		this.click(linkTx);
		WebElement objNumDoc;
		do { // ESPERA A QUE SE MUESTRE EL N�MERO DE DOCUMENTO AL INGRESAR AL DETALLE
			objNumDoc = this.element(locNumDocDet);
		} while (objNumDoc == null);
		Evidence.saveFullPage("DetalleTx" + numDocumento, this);
		return null;
	}
//***********************************************************************************************************************
	/**
	 * Este m�todo revisa en el listado de Transacciones realizadas en el Portal PYME, si existe la transacci�n cuyo
	 * n�mero de documento corresponde al recibido por par�metro, retornando el [WebElement]. En caso de no existir
	 * deja la evidencia de que NO existe. Si existe garantiza que la p�gina web se encuentra en la pantalla en donde
	 * se presenta la transacci�n. Deja almacenado en [this.isLinkNumDo] si el objeto es un link.
	 */
	private WebElement getElementNumDoc(String numDocumento) throws Exception {
		
		int numPage = 1;
		boolean continuarBusqueda = true;
		List<WebElement> listaTxs;
		String fechaHoy = DXCUtil.dateToString("dd/mm/yyyy") + " ";
		WebElement objNumDoc;
		do {
			// DETERMINA SI EL DATO DE LA TRANSACCI�N ES UN LINK O S�LO TEXTO:
			this.isLinkNumDoc = true;
			objNumDoc = this.element(xPathLinkTx.replace("TYPE", "a").replace("NUM_DOC", numDocumento));
			if (objNumDoc == null) {
				this.isLinkNumDoc = false;
				objNumDoc = this.element(xPathLinkTx.replace("TYPE", "span").replace("NUM_DOC", numDocumento));
			}
//-----------------------------------------------------------------------------------------------------------------------
			// NO SE ENCUENTRA LA TRANSACCI�N, SE BUSCA EN LA P�GINA SIGUIENTE, DESDE QUE SE PUEDA Y DESDE QUE LAS
			// SIGUIENTES TRANSACCIONES SEAN DE LA FECHA DE HOY
			if (objNumDoc == null) {
				Evidence.saveFullPage("NoEsta_"+ numDocumento + " (Pag" + numPage + ")", this);
				numPage++;
				continuarBusqueda = this.isDisplayed(locSiguiente);
				if (continuarBusqueda) {
					this.click(locSiguiente);
					// EN TEOR�A SELENIUM ESPERA LA CARGA DE LA PANTALLA
					listaTxs = this.findElements(By.xpath(xPathListaTxs.replace("FECHA", fechaHoy)));
					continuarBusqueda = ( listaTxs.size() > 0 ); // HAY TRANSACCIONES DE HOY
				}
			}
		} while (objNumDoc == null && continuarBusqueda);
		return objNumDoc;
	}
//***********************************************************************************************************************
}
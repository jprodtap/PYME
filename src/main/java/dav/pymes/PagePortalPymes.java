package dav.pymes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
import dxc.execution.BasePageWeb;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class PagePortalPymes extends BasePageWeb {

//=======================================================================================================================
	// LOCATORS Y EXPRESIONES PARA ENCONTRAR LOS OBJETOS EN FRONT PYMES
	By locCerrSes = By.cssSelector("a[id='CerrarSesion']"); // EN FRONT Y MIDDLE SE PRESENTA
	By locCmEmpresa = By.xpath("//select[@id='dropMasterEmpresa']");
	By locTitle = By.id("lblMasterTitulo");// "id:=lblMasterTitulo"; // TagName = "span"

	// LOCATORS MENU
	By Menu = By.id("mnMenu");
	By dflMenu = By.xpath("//a[@class='Nivel1']"); // Diferencia el menu ya que cambia el locator por mnMenu_1 Nivel1
													// mnMenu_3

	// XPATH QUE REMPLAZAN EL TEXTO CONTENIDO ('NB_MENU_INICIAL','NB_SUBMENU') POR
	// EL QUE DESEAMOS BUSCAR
	String xPathLocMenu = "//a[@class='Nivel1'][text()='NB_MENU_INICIAL']//parent::td";
	String xPathLocSubMenu = "//a[@class='Nivel2'][text()='NB_SUBMENU']//parent::td";

	// XPATH YA QUE AL REALIZAR UN PAGO CAMBIA EL LOCATOR
	String xPathLocMenuDF = "//a[@class='mnMenu_1 Nivel1 mnMenu_3'][text()='NB_MENU_INICIAL']//parent::td";
	String xPathLocSubMenuDF = "//a[@class='mnMenu_1 Nivel2 mnMenu_5'][text()='NB_SUBMENU']//parent::td";


	By table = By.xpath("//*[@id=\"cphCuerpo_panConfirmacion\"]/table/tbody/tr[1]/td/table/tbody/tr[7]/td/table/tbody/tr[2]/td");
	By table2 = By.xpath("//*[@id='Pnl_cuerpo']/table/tbody/tr[2]/td/table/tbody/tr[9]/td");
	By table3 = By.xpath("//*[@id='cphCuerpo_gvTransaccionesMasivas']");
	By table4 = By.xpath("//*[@id='cphCuerpo_gvDestinoFondos']");
	
	
	//Divisas
	By cmpPopup = By.id("mensaje");
	By btnPopup = By.xpath("//*[@id='botonModal' or @id='alertamodalbtn']");
	By btnPopupaceptar = By.id("btnmodalaceptar");
	By btnPopupAvertaceptar = By.xpath("//*[@id='AlertaModal']/div//button[contains(text(), 'Aceptar')]");
	By btnModalPopup = By.id("btnmodal2");
	By btnPopup2 = By.xpath("//*[@id='btnmodal']");
	By btnAcetarAler = By.id("alertamodalbtn");
	
	By AlertPopup = By.xpath("//*[@id='AlertaModal']/div");
	By AlertPopupAdver = By.xpath("//*[@id='AlertaModalComponente']/div");

	String Alermod = "//*[@id='AlertaModal']/div/div/div[2]/p";
	String Alermod2 = "//*[@id='AlertaModal']/div";

	

	protected PageLoginPymes pageLogin;

//=======================================================================================================================
	/**
	 * Este es el ï¿½nico constructor que tiene esta [BasePageWeb] ya que por sï¿½
	 * sola no puede existir, debe depender de otra [BasePageWeb] que ya haya sido
	 * abierta y que direccione a ï¿½sta.
	 */
	public PagePortalPymes(BasePageWeb parentPage) {
		super(parentPage);
		this.pageLogin = new PageLoginPymes(parentPage);
	}
//***********************************************************************************************************************
	/**
	 * Mï¿½todo para hacer el cierre de la sesiï¿½n.
	 */

	/*
	 * @ZEA public void closeSession() throws Exception {
	 * this.pageLogin.closeSession(); }
	 */

	// ***********************************************************************************************************************
	/**
	 * Este mÃ©todo permite terminar una iteraciÃ³n transaccional que se estÃ©
	 * realizando en Davicom.<br>
	 * Se reporta el evento [eventStatus] con el [mensaje] y se guarda evidencia,
	 * revisa si se debe cerrar sesiÃ³n antes de terminar la iteraciÃ³n actual.
	 */
	public void terminarIteracion(int eventStatus, String mensaje) throws Exception {
		Reporter.reportEvent(eventStatus, mensaje);
//		Evidence.save("AntesDeTerminar", this);
		Evidence.save("AntesDeTerminar");
		// CIERRA SESIÃN SI ES TIEMPO DE CERRARLA
		this.terminarIteracion();
	}

//***********************************************************************************************************************
	/**
	 * Estï¿½ mï¿½todo termina la iteraciï¿½n actual que se estï¿½ realizando en
	 * PortalPyme.<br>
	 * Si es tiempo de cerrar sesiï¿½n, la cierra.<br>
	 * <b>OJO:</b> El cierre de sesiï¿½n garantiza el cierre del Browser.
	 */
	public void terminarIteracion() throws Exception {
		this.getDriver().switchTo().defaultContent();
		this.pageLogin.terminarIteracion();
	}

//***********************************************************************************************************************
	/**
	 * Intenta seleccionar la empresa.<br>
	 * Retorna [null] si pudo hacer la selecciï¿½n, en caso contrario retorna
	 * mensaje de error.
	 * 
	 * @throws Exception
	 */
	public String seleccionarEmpresa(String nbEmpresa) throws Exception {
		int contador = 1;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador > 650) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut en el Login");
				SettingsRun.exitTestIteration();
			}
		} while (this.element(locCmEmpresa) == null);
		this.closeActiveCtas(); // CIERRA POPUP DE ACTIVE CUENTAS SI SE PRESENTA
		DXCUtil.wait(1);

		String[] empresasContra = this.getListItems(locCmEmpresa);

		if (nbEmpresa.equals("MANTENIMINETO DE EQUIPOS DE SEGURI")
				|| nbEmpresa.equals("MANTENIMINETO DE EQUIPOS DE SE GURIDAD")
				|| nbEmpresa.equals("MANTENIMINETO DE EQUIPOS DE SEGUIR")) {

			for (String empresas : empresasContra) {

				if (empresas.contains("MANTENIMINETO DE EQUIPOS DE SEGURI")
						|| empresas.contains("MANTENIMINETO DE EQUIPOS DE SE GURIDAD")
						|| empresas.contains("MANTENIMINETO DE EQUIPOS DE SEGUIR")
						|| empresas.contains("MANTENIMINETO DE EQUIPOS DE SE GUR")) {
					nbEmpresa = empresas;

				}
			}
		}

		if (nbEmpresa.equals("EMP AUTO DOS") || nbEmpresa.equals("EMP PYME AUTO DOS")) {

			for (String empresas : empresasContra) {

				if (empresas.contains("EMP AUTO DOS") || empresas.contains("EMP PYME AUTO DOS")) {
					nbEmpresa = empresas;

				}
			}
		}

		if (nbEmpresa.equals("CAMILO QUEVEDO QUEVEDO") || nbEmpresa.equals("CAMILO TORRES")) {

			for (String empresas : empresasContra) {

				if (empresas.contains("CAMILO QUEVEDO QUEVEDO") || empresas.contains("CAMILO TORRES")) {
					nbEmpresa = empresas;

				}
			}
		}

		String msgError = this.selectListItem(this.element(locCmEmpresa), nbEmpresa);

		if (!msgError.isEmpty()) {
			for (String empresas : empresasContra) {
				Reporter.reportEvent(Reporter.MIC_INFO, "*** Empresas: [" + empresas + "]");
			}
//			Evidence.save(msgError, this);
			Evidence.save(msgError);
			return "Seleccionando empresa : " + msgError;
		}
		this.closeActiveCtas(); // CIERRA POPUP DE ACTIVE CUENTAS SI SE PRESENTA
		return null;
	}

//***********************************************************************************************************************
	/**
	 * Mï¿½todo que permite moverse a travï¿½s de los menï¿½es presentados en
	 * Middle. Abriendo el menï¿½ dado por [opcMenu] y siguiendo la ruta por las
	 * opciones enviadas en [opcSubMenu], dando click en el ï¿½ltimo elemento de
	 * [opcSubMenu], en caso que no hayan sido enviados, da click directamente en
	 * [opcMenu].<br>
	 * [title] puede ser [null], VACIO o un valor; cuando se envï¿½a valor este
	 * mï¿½todo espera a que el tï¿½tulo enviado se presente en pantalla <b>OJO</b>
	 * sï¿½lo si el tï¿½tulo corresponde a un elemento con ID 'lblMasterTitulo'.<br>
	 * El retorno es [null] si se pudo ir a la opciï¿½n deseada, en caso contrario
	 * envï¿½a un error, los errores que se pueden presentar, son porque no se
	 * encuentran las opciones enviadas.
	 */
	public String irAOpcion(String title, String opcMenu, String opcSubMenu, String... opcSubMenu2) throws Exception {
		String xPath = "";
		if (this.isDisplayed(Menu)) {
			if (this.isDisplayed(dflMenu)) {
				xPath = xPathLocMenu.replace("NB_MENU_INICIAL", opcMenu);
			} else {
				xPath = xPathLocMenuDF.replace("NB_MENU_INICIAL", opcMenu);
			}
			String pathMenu = opcMenu;
			WebElement elemtMenu = this.element(xPath);
			if (elemtMenu == null) {
//				Evidence.save("ErrorMenu", this);
				Evidence.save("ErrorMenu");
				return "No se encontro en el Menu [" + pathMenu + "] - Valide la información";
			}
// -----------------------------------------------------------------------------------------------------------------------
			// SI LLEGA A ESTE PUNTO PUEDE IR SELECCIONANDO LAS OTRAS OPCIONES
			this.mouseOver(elemtMenu);
			List<WebElement> listaElements;
			if (opcSubMenu != null) {

				for (int numOpc = 0; numOpc < opcSubMenu.length(); numOpc++) {

					if (this.isDisplayed(dflMenu)) {
						xPath = xPathLocSubMenu.replace("NB_SUBMENU", opcSubMenu);
					} else {
						xPath = xPathLocSubMenuDF.replace("NB_SUBMENU", opcSubMenu);
					}
					listaElements = this.findElements(By.xpath(xPath));
					if (listaElements.size() == 0) {
//						Evidence.save("ErrorMenu", this);
						Evidence.save("ErrorMenu");
						return "En el menu [" + pathMenu + "] NO se encontro la opci�n [" + opcSubMenu
								+ "] - Valide la informacici�n";
					} else if (listaElements.size() > 1)
						return "Existen muchos submenus que contienen [" + opcSubMenu + "] en el WebPage";
					if (!this.isDisplayed(listaElements.get(0))) {

						do { // ESPERA A QUE SE HAYA DESPLEGADO EL ELEMENTO
							DXCUtil.wait(1);
						} while (!this.isDisplayed(listaElements.get(0)));
					}
					pathMenu += "/" + opcSubMenu;
					// NO SE PUEDE CON [listaElements.get(0)] PORQUE SE PRESENTA UN
					// [MoveTargetOutOfBoundsException]
					this.mouseOver(this.element(xPath)); // POSICIONA EL MOUSE SOBRE EL ELEMENTO RESPECTIVO
				}
			}
			if (opcSubMenu2 != null) {
				for (int numOpc = 0; numOpc < opcSubMenu2.length; numOpc++) {
					if (this.isDisplayed(dflMenu)) {
						xPath = xPathLocSubMenu.replace("NB_SUBMENU", opcSubMenu2[numOpc]);
					} else {
						xPath = xPathLocSubMenuDF.replace("NB_SUBMENU", opcSubMenu2[numOpc]);
					}
					listaElements = this.findElements(By.xpath(xPath));
					if (listaElements.size() == 0) {
//						Evidence.save("ErrorMenu", this);
						Evidence.save("ErrorMenu");
						return "En el menu [" + pathMenu + "] NO se encontro la opción [" + opcSubMenu2[numOpc]
								+ "] - Valide la información";
					} else if (listaElements.size() > 1)
						return "Existen muchos submenus que contienen [" + opcSubMenu2[numOpc] + "] en el WebPage";

					if (!this.isDisplayed(listaElements.get(0))) {
						do { // ESPERA A QUE SE HAYA DESPLEGADO EL ELEMENTO
							DXCUtil.wait(1);
						} while (!this.isDisplayed(listaElements.get(0)));
					}
					pathMenu += "/" + opcSubMenu2[numOpc];
					// NO SE PUEDE CON [listaElements.get(0)] PORQUE SE PRESENTA UN
					// [MoveTargetOutOfBoundsException]
					this.mouseOver(this.element(xPath)); // POSICIONA EL MOUSE SOBRE EL ELEMENTO RESPECTIVO
				}
			}
//			Evidence.save("Menu", this);
			Evidence.save("Menu");
			this.mouseClick();
// -----------------------------------------------------------------------------------------------------------------------
			// ESPERA LA MUESTRA DEL Tï¿½TULO [title]
			if (title != null && !title.isEmpty()) {
				this.ValidacionPreguntasPep();
				this.waitIngresoModulo(title);
//				Evidence.save("IngresoModulo", this);
				Evidence.save("IngresoModulo");
			}
		}
		return null;
	}

//***********************************************************************************************************************
	/**
	 * M�todo que permite moverse a trav�s de los men�es presentados en Middle.
	 * Abriendo el men� dado por [opcMenu] y siguiendo la ruta por las opciones
	 * enviadas en [opcSubMenu], dando click en el �ltimo elemento de [opcSubMenu],
	 * en caso que no hayan sido enviados, da click directamente en [opcMenu].<br>
	 * [title] puede ser [null], VACIO o un valor; cuando se env�a valor este m�todo
	 * espera a que el t�tulo enviado se presente en pantalla <b>OJO</b> s�lo si el
	 * t�tulo corresponde a un elemento con ID 'lblMasterTitulo'.<br>
	 * El retorno es [null] si se pudo ir a la opci�n deseada, en caso contrario
	 * env�a un error, los errores que se pueden presentar, son porque no se
	 * encuentran las opciones enviadas.
	 */
	public String irAOpcionMoZilla(String title, String opcMenu, String opcSubMenu, String opcSubMenu2,
			String opcSubMenu3) throws Exception {
		String xPath = "";

		int contador = 0;
		this.refresh();
		DXCUtil.wait(10);

		do {
			DXCUtil.wait(1);
			contador++;
			if (contador >= 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut");
				this.terminarIteracion();
			}
		} while (this.element(Menu) == null);

		if (this.isDisplayed(Menu)) {
			if (this.isDisplayed(dflMenu)) {
				xPath = xPathLocMenu.replace("NB_MENU_INICIAL", opcMenu);
			} else {
				xPath = xPathLocMenuDF.replace("NB_MENU_INICIAL", opcMenu);
			}
			String pathMenu = opcMenu;
			WebElement elemtMenu = this.element(xPath);
			if (elemtMenu == null) {
//				Evidence.save("ErrorMenu", this);
				Evidence.save("ErrorMenu");
				return "No se encontro en el Menu [" + pathMenu + "] - Valide la información";
			}
// -----------------------------------------------------------------------------------------------------------------------
			// SI LLEGA A ESTE PUNTO PUEDE IR SELECCIONANDO LAS OTRAS OPCIONES
			do {
				DXCUtil.wait(1);

			} while (!elemtMenu.isDisplayed());
			this.getJse().executeScript(
					"var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true, view: window}); arguments[0].dispatchEvent(event);",
					elemtMenu);
			List<WebElement> listaElements;
			if (opcSubMenu != null) {
//				for (int numOpc = 0; numOpc < opcSubMenu.length(); numOpc++) {

				if (this.isDisplayed(dflMenu)) {
					xPath = xPathLocSubMenu.replace("NB_SUBMENU", opcSubMenu);
				} else {
					xPath = xPathLocSubMenuDF.replace("NB_SUBMENU", opcSubMenu);
				}
				listaElements = this.findElements(By.xpath(xPath));
				if (listaElements.size() == 0) {
//					Evidence.save("ErrorMenu", this);
					Evidence.save("ErrorMenu");
					return "En el menu [" + pathMenu + "] NO se encontró la opción [" + opcSubMenu
							+ "] - Valide la informaci�n";
				} else if (listaElements.size() > 1)
					return "Existen muchos submenus que contienen [" + opcSubMenu + "] en el WebPage";
				if (!this.isDisplayed(listaElements.get(0))) {

					do { // ESPERA A QUE SE HAYA DESPLEGADO EL ELEMENTO
						DXCUtil.wait(1);
					} while (!this.isDisplayed(listaElements.get(0)));
				}
				pathMenu += "/" + opcSubMenu;
				// NO SE PUEDE CON [listaElements.get(0)] PORQUE SE PRESENTA UN
				// [MoveTargetOutOfBoundsException]
//				DXCUtil.wait(3);
				elemtMenu = this.element(xPath);
//				this.mouseOver(this.element(xPath)); // POSICIONA EL MOUSE SOBRE EL ELEMENTO RESPECTIVO
				do {
					DXCUtil.wait(1);
					contador++;
					if (contador >= 30) {
						Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut");
						this.terminarIteracion();
					}
				} while (this.element(elemtMenu) == null);
				this.getJse().executeScript(
						"var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true, view: window}); arguments[0].dispatchEvent(event);",
						elemtMenu);
				DXCUtil.wait(4);
			}
//			}
			if (opcSubMenu2 != null) {
//				for (int numOpc = 0; numOpc < opcSubMenu2.length; numOpc++) {

				if (this.isDisplayed(dflMenu)) {
					xPath = xPathLocSubMenu.replace("NB_SUBMENU", opcSubMenu2);
				} else {
					xPath = xPathLocSubMenuDF.replace("NB_SUBMENU", opcSubMenu2);
				}
				listaElements = this.findElements(By.xpath(xPath));
				if (listaElements.size() == 0) {
//					Evidence.save("ErrorMenu", this);
					Evidence.save("ErrorMenu");
					return "En el menu [" + pathMenu + "] NO se encontro la opción [" + opcSubMenu2
							+ "] - Valide la información";
				} else if (listaElements.size() > 1)
					return "Existen muchos submenus que contienen [" + opcSubMenu2 + "] en el WebPage";

				if (!this.isDisplayed(listaElements.get(0))) {
					do { // ESPERA A QUE SE HAYA DESPLEGADO EL ELEMENTO
						DXCUtil.wait(1);
					} while (!this.isDisplayed(listaElements.get(0)));
				}
				pathMenu += "/" + opcSubMenu2;
				// NO SE PUEDE CON [listaElements.get(0)] PORQUE SE PRESENTA UN
				// [MoveTargetOutOfBoundsException]
				DXCUtil.wait(3);
//				this.mouseOver(this.element(xPath)); // POSICIONA EL MOUSE SOBRE EL ELEMENTO RESPECTIVO
				elemtMenu = this.element(xPath);
				DXCUtil.wait(5);
				this.getJse().executeScript(
						"var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true, view: window}); arguments[0].dispatchEvent(event);",
						elemtMenu);
//				}
				DXCUtil.wait(3);
			}
			if (opcSubMenu3 != null) {
//				for (int numOpc = 0; numOpc < opcSubMenu3.length; numOpc++) {

				if (this.isDisplayed(dflMenu)) {
					xPath = xPathLocSubMenu.replace("NB_SUBMENU", opcSubMenu3);
				} else {
					xPath = xPathLocSubMenuDF.replace("NB_SUBMENU", opcSubMenu3);
				}
				listaElements = this.findElements(By.xpath(xPath));
				if (listaElements.size() == 0) {
//					Evidence.save("ErrorMenu", this);
					Evidence.save("ErrorMenu");
					return "En el menu [" + pathMenu + "] NO se encontro la opcion [" + opcSubMenu3
							+ "] - Valide la información";
				} else if (listaElements.size() > 1)
					return "Existen muchos submenus que contienen [" + opcSubMenu3 + "] en el WebPage";

				if (!this.isDisplayed(listaElements.get(0))) {
					do { // ESPERA A QUE SE HAYA DESPLEGADO EL ELEMENTO
						DXCUtil.wait(1);
					} while (!this.isDisplayed(listaElements.get(0)));
				}
				pathMenu += "/" + opcSubMenu3;
				// NO SE PUEDE CON [listaElements.get(0)] PORQUE SE PRESENTA UN
				// [MoveTargetOutOfBoundsException]
//					DXCUtil.wait(3);
//				this.mouseOver(this.element(xPath)); // POSICIONA EL MOUSE SOBRE EL ELEMENTO RESPECTIVO
				elemtMenu = this.element(xPath);
				DXCUtil.wait(2);
				this.getJse().executeScript(
						"var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true, view: window}); arguments[0].dispatchEvent(event);",
						elemtMenu);
//				}
				DXCUtil.wait(3);
			}
			elemtMenu.click();
//			this.getJse().executeScript("arguments[0].click();", elemtMenu);
//			this.mouseClick();
// -----------------------------------------------------------------------------------------------------------------------
			// ESPERA LA MUESTRA DEL TITULO [title]
			if (title != null && !title.isEmpty()) {
				this.waitIngresoModulo(title);
//				Evidence.save("IngresoModulo", this);
				Evidence.save("IngresoModulo");
			}
		}
		return null;
	}

//***********************************************************************************************************************

	/**
	 * Metodo que da click en la opcion de menï¿½ 'Inicio'.
	 */
	public void irAInicio() throws Exception {
		this.irAOpcion("Saldos Consolidados", "Inicio", null);
	}

//***********************************************************************************************************************
	/**
	 * Mï¿½todo que espera a que se presente el Tï¿½TULO [titleModulo].<br>
	 * Se presenta Excepciï¿½n si supera los 60 segundos de espera.<br>
	 * Este mï¿½todo sirve sï¿½lo cuando el tï¿½tulo corresponde al estï¿½ndar
	 * Middle que no tiene que moverse a ningï¿½n frame y buscar el elemento cuyo ID
	 * sea "lblMasterTitulo"
	 */
	public void waitIngresoModulo(String titleModulo) throws Exception {
		int time = 1;
		boolean esTitleEsperado = false;
		WebElement elemTitle;
		titleModulo = titleModulo.trim();
		do {
			DXCUtil.wait(1);
			elemTitle = this.element(locTitle);
			if (elemTitle != null)
				esTitleEsperado = DXCUtil.equalsIgnoreCaseAndAccents(titleModulo, elemTitle.getText().trim());

			if (!esTitleEsperado && this.retornoALogueo())
				return; // TERMINA EL CICLO

			time++;
		} while (!esTitleEsperado && time <= 60);
		if (!esTitleEsperado) {
			throw new Exception(
					"PageInicioMiddle ERROR -- Se supera los 60 segundos esperando ingreso al modulo con titulo ["
							+ titleModulo + "]");
		}
	}

	// ***********************************************************************************************************************
	/**
	 * Mï¿½todo que espera a que se presente el Tï¿½TULO [titleModulo].<br>
	 * Se presenta Excepciï¿½n si supera los 60 segundos de espera.<br>
	 * Este mï¿½todo sirve sï¿½lo cuando el tï¿½tulo corresponde al estï¿½ndar
	 * Middle que no tiene que moverse a ningï¿½n frame y buscar el elemento cuyo ID
	 * sea "lblMasterTitulo"
	 */
	public boolean waitEspetaTitulo(String titleModulo) throws Exception {
		int time = 1;
		boolean esTitleEsperado = false;
		WebElement elemTitle;
		titleModulo = titleModulo.trim();
		do {
			DXCUtil.wait(1);
			elemTitle = this.element(locTitle);
			if (elemTitle != null)
				esTitleEsperado = DXCUtil.equalsIgnoreCaseAndAccents(titleModulo, elemTitle.getText().trim());

			if (!esTitleEsperado && this.retornoALogueo())
				return esTitleEsperado; // TERMINA EL CICLO

			time++;
		} while (!esTitleEsperado && time <= 30);

		return esTitleEsperado;
	}

//***********************************************************************************************************************
	public void closeActiveCtas() throws Exception {
		WebElement popUpCtas = this.element("//div[@id='cphCuerpo_pnlMensaje']");
		if (this.isDisplayed(popUpCtas)) {
//			Evidence.save("PopupActiveCtas", this);
			Evidence.save("PopupActiveCtas");
			this.clickNoScrollIntoView(this.element("//button[@class='close']"));
		}
	}

//***********************************************************************************************************************
	/**
	 * Este mï¿½todo retorna [true] si la pantalla se encuentra en la de logueo,
	 * como que se saliï¿½ del portal Middle.<br>
	 * Adicional deja almacenado en [estaEnLogueo] el valor respectivo.
	 */
	private boolean retornoALogueo() {
		/*
		 * WebElement objTituloInicio = this.element("//td[@class='Titulo']");
		 * this.estaEnLogueo = false; if (this.isDisplayed(objTituloInicio))
		 * this.estaEnLogueo =
		 * objTituloInicio.getText().contains("Bienvenido al Portal PYMES"); return
		 * this.estaEnLogueo;
		 */
		return false;
	}

//***********************************************************************************************************************
	/**
	 * Mï¿½todo que retorna el mensaje de alerta si este existe. Si el retorno es
	 * [null] es porque NO existe un mensaje de alerta.<br>
	 * Se recibe por parï¿½metro un listado de los posibles id de las alertas que se
	 * pueden presentar.
	 */
	public String getMsgAlertIfExist(String... idsAlerta) {
		String msgAlert = null;
		for (String id : idsAlerta) {
			By locMessage = By.id(id);
			if (this.element(locMessage) != null) {
				if (this.isDisplayed(locMessage)) {
					DXCUtil.wait(1);
					try {
						msgAlert = this.getText(locMessage).trim();
					} catch (Exception e) {
						msgAlert = null;
					}
					if (msgAlert == null || msgAlert.equals("")) // NO HAY MENSAJE
						msgAlert = null;
					else
						break; // PARA TERMINAR EL CICLO
				}
			}
		}
		return msgAlert;
	}

// ***********************************************************************************************************************
	/**
	 * Metodo que retorna el mensaje de alerta si este existe. Si el retorno es
	 * [null] es porque NO existe un mensaje de alerta.<br>
	 * Se recibe por parametro un listado de los posibles id de las alertas que se
	 * pueden presentar.
	 */
	public String getMsgAlertIfExistxPath(String... xPathExpressions) {
		String msgAlert = null;

		for (String xPathExpression : xPathExpressions) {
			WebElement element = this.element(By.xpath(xPathExpression));

			if (element != null) {
				DXCUtil.wait(1);
				try {
					msgAlert = element.getText().trim();
				}catch (Exception e) {
					msgAlert = null;
				}

				if (msgAlert == null || msgAlert.equals("")) {
					msgAlert = null;
				} else {
					break; // PARA TERMINAR EL CICLO
				}
			}
		}

		return msgAlert;
	}


	public String getTextMid() throws Exception {
		WebElement getexto = null;
		DXCUtil.wait(3);
		try {
			getexto = this.findElement(By.xpath("//*[contains(text(), 'Pagos')]"));

		} catch (Exception e) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se pudo encontrar el modulo de Pagos, Transferenia  y otros");
			this.terminarIteracion();
		}
		String texto = this.getText(getexto);
		return texto;
	}

//***********************************************************************************************************************

	public String irAOpcionIframe(String title, String opcMenu, String opcSubMenu, String... opcSubMenu2)
			throws Exception {
		String xPath = "";
		if (this.isDisplayed(Menu)) {
			if (this.isDisplayed(dflMenu)) {
				xPath = xPathLocMenu.replace("NB_MENU_INICIAL", opcMenu);
			} else {
				xPath = xPathLocMenuDF.replace("NB_MENU_INICIAL", opcMenu);
			}
			String pathMenu = opcMenu;
			WebElement elemtMenu = this.element(xPath);
			if (elemtMenu == null) {
//				Evidence.save("ErrorMenu", this);
				Evidence.save("ErrorMenu");
				return "No se encontró en el Menu [" + pathMenu + "] - Valide la información";
			}
// -----------------------------------------------------------------------------------------------------------------------
			// SI LLEGA A ESTE PUNTO PUEDE IR SELECCIONANDO LAS OTRAS OPCIONES
			this.mouseOver(elemtMenu);
			List<WebElement> listaElements;
			if (opcSubMenu != null) {
				for (int numOpc = 0; numOpc < opcSubMenu.length(); numOpc++) {

					if (this.isDisplayed(dflMenu)) {
						xPath = xPathLocSubMenu.replace("NB_SUBMENU", opcSubMenu);
					} else {
						xPath = xPathLocSubMenuDF.replace("NB_SUBMENU", opcSubMenu);
					}
					listaElements = this.findElements(By.xpath(xPath));
					if (listaElements.size() == 0) {
//						Evidence.save("ErrorMenu", this);
						Evidence.save("ErrorMenu");
						return "En el menu [" + pathMenu + "] NO se encontrola opción [" + opcSubMenu
								+ "] - Valide la información";
					} else if (listaElements.size() > 1)
						return "Existen muchos submenus que contienen [" + opcSubMenu + "] en el WebPage";
					if (!this.isDisplayed(listaElements.get(0))) {

						do { // ESPERA A QUE SE HAYA DESPLEGADO EL ELEMENTO
							DXCUtil.wait(1);
						} while (!this.isDisplayed(listaElements.get(0)));
					}
					pathMenu += "/" + opcSubMenu;
					// NO SE PUEDE CON [listaElements.get(0)] PORQUE SE PRESENTA UN
					// [MoveTargetOutOfBoundsException]
					this.mouseOver(this.element(xPath)); // POSICIONA EL MOUSE SOBRE EL ELEMENTO RESPECTIVO
				}
			}
			if (opcSubMenu2 != null) {
				for (int numOpc = 0; numOpc < opcSubMenu2.length; numOpc++) {
					if (this.isDisplayed(dflMenu)) {
						xPath = xPathLocSubMenu.replace("NB_SUBMENU", opcSubMenu2[numOpc]);
					} else {
						xPath = xPathLocSubMenuDF.replace("NB_SUBMENU", opcSubMenu2[numOpc]);
					}
					listaElements = this.findElements(By.xpath(xPath));
					if (listaElements.size() == 0) {
//						Evidence.save("ErrorMenu", this);
						Evidence.save("ErrorMenu");
						return "En el menu [" + pathMenu + "] NO se encontro la opción [" + opcSubMenu2[numOpc]
								+ "] - Valide la información";
					} else if (listaElements.size() > 1)
						return "Existen muchos submenus que contienen [" + opcSubMenu2[numOpc] + "] en el WebPage";

					if (!this.isDisplayed(listaElements.get(0))) {
						do { // ESPERA A QUE SE HAYA DESPLEGADO EL ELEMENTO
							DXCUtil.wait(1);
						} while (!this.isDisplayed(listaElements.get(0)));
					}
					pathMenu += "/" + opcSubMenu2[numOpc];
					// NO SE PUEDE CON [listaElements.get(0)] PORQUE SE PRESENTA UN
					// [MoveTargetOutOfBoundsException]
					this.mouseOver(this.element(xPath)); // POSICIONA EL MOUSE SOBRE EL ELEMENTO RESPECTIVO
				}
			}
			this.mouseClick();
// -----------------------------------------------------------------------------------------------------------------------
			// ESPERA LA MUESTRA DEL Tï¿½TULO [title]
			if (title != null && !title.isEmpty()) {
				this.waitIngresoModulo(title);
				/*
				 * if (this.estaEnLogueo) { // SE CERRï¿½ EL PORTAL POR TIEMPO, TOCA LOGUEARSE
				 * DE NUEVO this.pageLogin.loginMiddle(); return this.irAOpcion(title, opcMenu,
				 * opcSubMenu); }
				 */
//				Evidence.save("IngresoModulo", this);
				Evidence.save("IngresoModulo");
			}
		}
		return null;
	}

	public String lisProducto(By locator, String tipoProducto, String numeroProducto) throws Exception {
		int contador = 0;
		String msgError = null;
		do {
			DXCUtil.wait(7);
			contador++;
			if (contador > 3) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro la lista: Producto");
				SettingsRun.exitTestIteration();
			}
		} while (this.element(locator) == null);
//		String ptoduct = tipoProducto + " ************" + DXCUtil.right(numeroProducto, 4);
		String[] lista = this.getListItems(this.element(locator));
		for (String lis : lista) {
			Reporter.write(lis);
		}

		if (tipoProducto.equals("Mastercard") || tipoProducto.equals("mastercard")) {
			for (String list : lista) {
				if (list.contains("mastercard")) {
					tipoProducto = "mastercard";
				}
			}
		}

		msgError = this.selectListContainsItems(this.element(locator), tipoProducto, " ************",
				DXCUtil.right(numeroProducto, 4));
//		Evidence.save("Producto Selecionado", this);
		Evidence.save("Producto Selecionado");
		String msg = DXCUtil.left(msgError, 58);
		if (!msgError.isEmpty() && !msg.contains(DXCUtil.right(numeroProducto, 4))) {
			return "Seleccionando producto : " + msgError;
		}

		msgError = this.getMsgAlertIfExist("lblMasterAlerta");
		if (msgError != null && !msgError.isEmpty()) {
//			Evidence.save(msgError, this);
			Evidence.save(msgError);
			return msgError;
		}
		return null;
	}

	/**
	 * Metodo extrae los datos ingresados
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] CuentaDesMotor() throws Exception {
		// Encuentra el elemento de la tabla por su ID (asumiendo que la tabla tiene un
		// ID)
		DXCUtil.wait(5);
		WebElement tablaTx = null;
		String[] movi = null;
		if (this.element(table) != null) {
			tablaTx = this.findElement(table);
		}
		if (this.element(table2) != null) {
			tablaTx = this.findElement(table2);
		}
		if (this.element(table3) != null) {
			tablaTx = this.findElement(table3);
		}
		if (this.element(table4) != null) {
			tablaTx = this.findElement(table4);
		}

		int contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador == 29) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro la Tabla de registros");
			}
		} while (this.element(tablaTx) == null && contador <= 30);

		if (this.element(tablaTx) != null) {

			// Encuentra los encabezados y mapea sus posiciones
			Map<String, Integer> mapHeaders = new HashMap<>();
			List<WebElement> encabezados = tablaTx.findElements(By.xpath("//tr[1]/th"));
			if (encabezados.isEmpty()) {
				encabezados = tablaTx.findElements(By.xpath("//tr[1]/td")); // fallback si vienen como <td>
			}
			for (int i = 0; i < encabezados.size(); i++) {
				String header = encabezados.get(i).getText();
				mapHeaders.put(header, i);
			}

			// Encuentra todas las filas (excluyendo la de encabezados)
			List<WebElement> filas = tablaTx.findElements(By.xpath("//tr[@class='CeldaGrilla']"));
			movi = new String[filas.size()];

			// Recorre cada fila
			for (int i = 0; i < filas.size(); i++) {
				WebElement fila = filas.get(i);
				List<WebElement> celdas = fila.findElements(By.tagName("td"));

				// Obtiene los posibles headers reales desde el DOM (por si tienen nombre
				// variable)
				String headerBancoDestino = getHeaderAlias("Banco destino","Banco Destino", "Banco","BANCO DESTINO","BANCO DESTINO", "BANCO");
				String headerIdentificacion = getHeaderAlias("Identificación", "identificación", "ID Destinatario","IDENTIFICACIÓN", "IDENTIFICACIÓN", "ID DESTINATARIO");
				String headerTitular = getHeaderAlias("Titular","titular","Títular","títular", "Nombre Destinatario","TITULAR", "TÍTULAR","NOMBRE DESTINATARIO");
				String headerTipodes = getHeaderAlias("Tipo destino","Tipo Destino","TIPO DESTINO");
				String headerNumerodes = getHeaderAlias( "Número destino","Número Destino","NÚMERO DESTINO");
				String headerEstado = getHeaderAlias( "Estado","estado","ESTADO");
				String headerMotivo = getHeaderAlias( "Motivo","motivo","MOTIVO");

				// Extrae valores con base en los headers encontrados
				String bancoDestino = getCellValue(celdas, mapHeaders, headerBancoDestino);
				String tipoDestino = getCellValue(celdas, mapHeaders, headerTipodes);
				String numeroDestino = getCellValue(celdas, mapHeaders,headerNumerodes);
				String titular = getCellValue(celdas, mapHeaders, headerTitular);
				String identificacion = getCellValue(celdas, mapHeaders, headerIdentificacion);
				String estado = getCellValue(celdas, mapHeaders, headerEstado);
				String motivo = getCellValue(celdas, mapHeaders, headerMotivo);
				


				if (motivo == null || motivo.isEmpty()) {
					motivo = getCellValue(celdas, mapHeaders, "Rechazo");
				}

				String valor = getCellValue(celdas, mapHeaders, "Valor");
				
				if (valor != null && valor.contains("$")) {
					
					
					valor = valor.replace("$", "").replace(".", "").trim();
					
					if (valor.contains(",")) {
						valor = valor.replace(",", ".").trim();
					}
//					if (valor.contains(",")) {
//						valor = valor.replace(",", ".").trim();
//					}
//
//					int numPoints = 0;
//					for (char c : valor.toCharArray()) {
//						if (c == '.')
//							numPoints++;
//					}
//					if (numPoints >= 2) {
//						int pos = valor.indexOf(".");
//						valor = valor.substring(0, pos) + valor.substring(pos + 1);
//					}
				}

				// Guarda el resultado
				movi[i] = bancoDestino + "|" + tipoDestino + "|" + valor + "|" + identificacion + "|" + numeroDestino
						+ "|" + estado + "|" + motivo;
			}
		}

		return movi;
	}

	private String getCellValue(List<WebElement> celdas, Map<String, Integer> mapHeaders, String headerName) {
		if (headerName == null)
			return null;
		Integer index = mapHeaders.get(headerName.trim());
		if (index != null && index < celdas.size()) {
			return celdas.get(index).getText();
		}
		return null;
	}

	private String getHeaderAlias(String... posiblesTextos) {
		for (String texto : posiblesTextos) {
			WebElement header = this.element("//table[@class='TablaGrilla']//a[contains(text(), '" + texto + "')]");
			if (header != null) {
				return header.getText();
			}
			
			 header = this.element("//table[@class='TablaGrilla']//tr/th[contains(text(), '" + texto + "')]");
			if (header != null) {
				return header.getText();
			}
		}
		return null;
	}

	public void ValidacionPreguntasPep() throws Exception {

		WebElement infoPep = this.element("//h2[contains(text(), 'Información PEP')]");
		if (this.isDisplayed(infoPep)) {
//			Evidence.save("Informaci�n PEP", this);
			Evidence.save("Información PEP");
			// Botones S� y No
			WebElement botonSi = this.element(By.id("rbSiPreguntaUno"));
			WebElement botonSi2 = this.element(By.id("rbSiPreguntaDos"));
			WebElement botonSi3 = this.element(By.id("rbNoPreguntaTres"));
			WebElement botonSi4 = this.element(By.id("rbNoPreguntaCuatro"));

			// Checkbox Acepto el tratamiento
			WebElement checkboxAcepto = this.element(By.id("cbTratamientoDatos"));

			// Bot�n Continuar
			WebElement botonContinuar = this.element(By.id("cphCuerpo_btnContinuar"));
			if (!botonSi.isSelected()) {
				String scriptClicIngresar1 = "document.querySelector(\"#rbSiPreguntaUno\").click();";
				this.getJse().executeScript(scriptClicIngresar1);
//				botonSi.click();
			}
			if (!botonSi2.isSelected()) {
				DXCUtil.wait(1);
				String scriptClicIngresar1 = "document.querySelector(\"#rbSiPreguntaDos\").click();";
				this.getJse().executeScript(scriptClicIngresar1);
//				botonSi.click();
			}
			if (!botonSi3.isSelected()) {
				DXCUtil.wait(4);
				String scriptClicIngresar1 = "document.querySelector(\"#rbNoPreguntaTres\").click();";
				this.getJse().executeScript(scriptClicIngresar1);
//				botonSi.click();
			}
			if (!botonSi4.isSelected()) {
				DXCUtil.wait(4);
				String scriptClicIngresar1 = "document.querySelector(\"#rbNoPreguntaTres\").click();";
				this.getJse().executeScript(scriptClicIngresar1);
//				botonSi.click();
			}

			if (!checkboxAcepto.isSelected()) {
				DXCUtil.wait(1);
				String scriptClicIngresar1 = "document.querySelector(\"#rbNoPreguntaCuatro\").click();";
				this.getJse().executeScript(scriptClicIngresar1);
//				checkboxAcepto.click();
			}
//			Evidence.save("Informaci�n PEP", this);
			Evidence.save("Información PEP");
			DXCUtil.wait(1);
			botonContinuar.click();

		}
	}
	
	
	// =========================================================================================================================================


		/**
		 * Metodo que retorna el mensaje de alerta si este existe en divisas. Si el
		 * retorno es [null] es porque NO existe un mensaje de alerta.<br>
		 * 
		 * @return
		 * @throws Exception
		 */
		public String getActiveIntAlert() throws Exception {
			int contador = 1;
			String msgResp;

			if (this.element(btnModalPopup) != null && this.isDisplayed(btnModalPopup) && this.isEnabled(btnModalPopup)) {
				do {
					DXCUtil.wait(1);
					contador++;
				} while (this.element(btnModalPopup) != null && !this.isDisplayed(btnModalPopup)
						&& !this.isEnabled(btnModalPopup) && contador <= 10);

				msgResp = this.getMsgAlertIfExist("AlertaModal");// cambiar el extraer mensaje popup

			} else if (this.element(btnPopup) != null && this.isDisplayed(cmpPopup) && this.isEnabled(btnPopup)) {
				do {
					DXCUtil.wait(1);
					contador++;
				} while (!this.isDisplayed(btnPopup) && !this.isEnabled(btnPopup) && contador <= 10);

				msgResp = this.getMsgAlertIfExist("mensaje");// cambiar el extraer mensaje popup
			} else if (this.element(AlertPopupAdver) != null && this.isDisplayed(AlertPopupAdver) && this.isEnabled(AlertPopupAdver)) {
				DXCUtil.wait(1);
				msgResp = this.element(AlertPopupAdver).getAttribute("outerText");
				
			} else {
				do {
					DXCUtil.wait(1);
					contador++;
				} while (this.element(btnPopup2) != null && !this.isDisplayed(btnPopup2) && !this.isEnabled(btnPopup2)
						&& contador <= 10);

				msgResp = this.getMsgAlertIfExistxPath(Alermod);// cambiar el extraer mensaje popup
			}

//			DXCUtil.wait(1);
			if (this.isDisplayed(btnModalPopup) && this.isEnabled(btnModalPopup)) {
				Evidence.saveAllScreens("Alert", this);
			} else if (this.isDisplayed(btnPopup2) && this.isEnabled(btnPopup2)) {
				Evidence.saveAllScreens("Alert", this);
			} else if (this.isDisplayed(btnPopup) && this.isDisplayed(cmpPopup) && this.isEnabled(btnPopup)) {
				Evidence.saveAllScreens("Alert", this);
			}else if (this.element(AlertPopupAdver) != null && this.isDisplayed(AlertPopupAdver) && this.isEnabled(AlertPopupAdver)) {
				Evidence.saveAllScreens("Alert", this);
				
			}

			return msgResp;
		}
		
		
		// =========================================================================================================================================
		
		
		/**
		 * Metodo que retorna el mensaje de alerta si este existe en divisas. Si el
		 * retorno es [null] es porque NO existe un mensaje de alerta.<br>
		 * 
		 * @return Mensaje Alerta del portal
		 * @throws Exception
		 */
		public String closeActiveIntAlert() throws Exception {
			int contador = 1;
			String msgResp = null;

			if (this.element(btnModalPopup) != null && this.isDisplayed(btnModalPopup) && this.isEnabled(btnModalPopup)) {
				do {
					DXCUtil.wait(1);
					contador++;
				} while (!this.isDisplayed(btnModalPopup) && !this.isEnabled(btnModalPopup) && contador <= 10);
				msgResp = this.getMsgAlertIfExist("AlertaModal");

			} else if (this.element(cmpPopup) != null && this.isDisplayed(cmpPopup) && this.isEnabled(btnPopup)) {
				do {
					DXCUtil.wait(1);
					contador++;
				} while (!this.isDisplayed(btnPopup) && !this.isEnabled(btnPopup) && contador <= 10);

				msgResp = this.getMsgAlertIfExist("mensaje");
			} else if (this.element(AlertPopupAdver) != null && this.isDisplayed(AlertPopupAdver)
					&& this.isEnabled(AlertPopupAdver)) {
				do {
					DXCUtil.wait(1);
					contador++;
				} while (!this.isDisplayed(AlertPopupAdver) && !this.isEnabled(AlertPopupAdver) && contador <= 10);

				msgResp = this.getMsgAlertIfExist("mensaje");
			} else {
				do {
					DXCUtil.wait(1);
					contador++;
				} while (this.element(btnPopup2) != null && !this.isDisplayed(btnPopup2) && !this.isEnabled(btnPopup2)
						&& contador <= 10);

				msgResp = this.getMsgAlertIfExistxPath(Alermod);
			}

			if (this.element(Alermod2) != null && this.isDisplayed(this.element(Alermod2))
					&& this.isEnabled(this.element(Alermod2))) {
				do {
					DXCUtil.wait(6);
					contador++;
				} while (!this.isDisplayed(this.element(Alermod2)) && !this.isEnabled(this.element(Alermod2))
						&& contador <= 10);
				msgResp = this.getMsgAlertIfExistxPath(Alermod2);// cambiar el extraer

			}

//			DXCUtil.wait(1);
			if (this.isDisplayed(btnModalPopup) && this.isEnabled(btnModalPopup)) {
				Evidence.saveAllScreens("Alert", this);
				this.click(btnModalPopup);
			} else if (this.isDisplayed(btnPopup2) && this.isEnabled(btnPopup2)) {
				Evidence.saveAllScreens("Alert", this);
				this.click(btnPopup2);
			} else if (this.isDisplayed(cmpPopup) && this.isEnabled(btnPopup)) {
				Evidence.saveAllScreens("Alert", this);
				this.click(btnPopup);
			} else if (this.isDisplayed(AlertPopupAdver) && this.isEnabled(AlertPopupAdver)) {
				Evidence.saveAllScreens("Alert", this);
				this.click(btnAcetarAler);
			} else if (this.isDisplayed(this.element(Alermod2)) && this.isEnabled(this.element(Alermod2))) {
				Evidence.saveAllScreens("Alert", this);
				if (this.element(btnPopupaceptar) != null)
					this.click(btnPopupaceptar);
				if (this.element(btnPopupAvertaceptar) != null)
					this.click(btnPopupAvertaceptar);
			}

			return msgResp;
		}
}
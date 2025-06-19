package dav.FiduciaEstrusturada;

import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;

import dxc.util.DXCUtil;

public class PageFiduciaFront extends BasePageWeb {

	/*
	 * Constructor y Pages
	 */
	PageOrigen pageOrigen = null;
	PageFormatosyDetalles pageFormatosyDetalles = null;

	public PageFiduciaFront(BasePageWeb parentPage) {
		super(parentPage);
	}

	/*
	 * Variables para realizar acciones
	 */
	static String[] msgRes = null;

	/*
	 * Locators Negocios fiduciarios
	 */
	static WebElement[] navLinkElement = null;

	public static By navFiducia = By.xpath("//*[@id='navbar']/ul");
	public static By labelSesionExiste = By.xpath("//*[text()='Sesión no existe o ha expirado por inactividad.']");
	public static By msgErrorUrl = By.xpath("//div[@class='wrapper_contenido']");

	By frameFiducia = By.id("cphCuerpo_iframePanel");
	By btnIzqNegFidu = By.xpath("//td/*[@value='Negocios Fiduciarios']");

	static Date fechaHoraTx;
	String navbar = "//ul[@class='nav navbar-nav']/li[@class='dropdown']/a[text()='{NAVBAR-OPTION}']";
	String navbar2 = "//ul[@class='nav navbar-nav']//a[text()='{NAVBAR-OPTION}']";
	String submenu = "//a[text()='{NAVBAR-OPTION}']/following-sibling::ul[@class='dropdown-menu']/li/a[text()='{SUBMENU}']";
	String submenu2 = "//li[@class='dropdown dropdown-submenu open']/ul//a[text()='{SUBMENU}']";

	/**
	 * Este metodo encargado de entrar al módulo de fiducia estructurada, realizando
	 * maximo 3 veces la prueba debido a un mensaje que suele aparecer, dependiendo
	 * el tipo de prueba a realizar se ejecutara un metodo diferente de otro page
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean fiduciaDocumentos() throws Exception {
		String tipoPrueba = SettingsRun.getTestData().getParameter("Tipo Prueba").trim();
		String tipoFormatoDes = SettingsRun.getTestData().getParameter("Formato de descarga").trim();
		String proyecto = SettingsRun.getTestData().getParameter("Código de Proyecto").trim();
		String informacionConsulta = SettingsRun.getTestData().getParameter("Información a consultar Informes")
				.trim();
		String informe = SettingsRun.getTestData().getParameter("Selección de informe").trim();

		boolean confirmarOp = false;
		int contador = 0;
		this.pageOrigen = new PageOrigen(this);
		this.pageFormatosyDetalles = new PageFormatosyDetalles(this);
		msgRes = new String[1];

		String ruta = this.pageOrigen.irAOpcion(null, "Consultas", "Negocios Fiduciarios");
		if (ruta != null) {
			JavascriptExecutor jse = (JavascriptExecutor) this.getDriver();
			WebElement btnNegociosFiducia = this.element(btnIzqNegFidu);
			jse.executeScript("arguments[0].click();", btnNegociosFiducia);
		}

		// Realizara la acción hasta que alguna de las condiciones dentro del do se
		// cumplan
		do {
			this.pageOrigen.changeToDefaultFrame();
			contador++;
			// recargara la pagina si la el array contiene información en la posición 0
			do {
				DXCUtil.wait(1);
				if (msgRes[0] != null)
					this.pageOrigen.refresh();
			} while (this.element(frameFiducia) == null);

//			Evidence.save("Fiducia Evidencias", this);
			Evidence.save("Fiducia Evidencias");

			if (this.element(frameFiducia) != null) {

				if (tipoPrueba.contains("Pendientes de descarga")) {
					msgRes = this.pageFormatosyDetalles.descargarDocumentosPendientes(tipoPrueba);
				} else if (tipoPrueba.contains("Descarga de formatos")) {
					msgRes = this.pageFormatosyDetalles.descargaFormatos(tipoPrueba);
				} else if (!tipoPrueba.contains("Informes"))
					msgRes = this.pageFormatosyDetalles.seccionFormatosYDetalles(tipoPrueba, tipoFormatoDes, proyecto);
				else
					msgRes = this.pageFormatosyDetalles.seccionInformes(tipoPrueba, tipoFormatoDes, proyecto,
							informacionConsulta, informe);

				//
				for (int i = 0; i < msgRes.length; i++) {

					if (tipoPrueba.contains("Pendientes de descarga")
							&& !msgRes[0].contains("Sesión no existe o ha expirado por inactividad")) {
						confirmarOp = true;
						break;
					} else if (msgRes[i] != null && (msgRes[i].contains("Descargado")
							|| msgRes[i].contains("descargado") || msgRes[i].contains("Su archivo se está procesando")))
						confirmarOp = (msgRes[i].contains("Descargado")
								|| msgRes[i].contains("Su archivo se está procesando"));
					else {
						Reporter.write("*** No se esperaba el mensaje: '" + msgRes[0]
								+ "', Se reintentara hacer la prueba nuevamente.");
						confirmarOp = false;
					}

					if (msgRes[i].contains("No se ha encontrado la opción") || msgRes[i].contains("Error data")
							|| msgRes[i].contains("No hay datos")) {
						// Se vuelve true para que se salga de la iteración y realice su debida
						// validación
						confirmarOp = true;
					}
				}

			} else {
				// No fue posible realizar la prueba, imprime un mensaje volviendo nuevamente a
				// la parte inicial del do/while
				if (this.element(labelSesionExiste) != null) {
					msgRes[0] = this.element(labelSesionExiste).getText();
					Reporter.write("*** No se esperaba el mensaje: '" + msgRes[0]
							+ "', Se reintentara hacer la prueba nuevamente.");
				}
			}
		} while (contador <= 2 && !confirmarOp);

		return confirmarOp;
	}

	/**
	 * Realiza el direccionamiento a la opción a la cual se va a realizar la
	 * solicitud del documento solicitado
	 * 
	 * @param tipoPrueba
	 * @return
	 * @throws Exception
	 */
	public String rutasFiducia(String tipoPrueba) throws Exception {
		String msgError = "";
//		Evidence.save(tipoPrueba + " Evidencias " + fechaHoraTx, this);
		Evidence.save(tipoPrueba + " Evidencias " + fechaHoraTx);

		int contador = 0;
		do {
			// Siempre tendra 3 campos, las posiciones se llenaran dependiendo el caso de
			// prueba
			navLinkElement = new WebElement[3];
			contador++;
			// Identifica el menu
			navbar = navbar.replace("{NAVBAR-OPTION}", ControllerFiducia.getMenu(tipoPrueba));
			navLinkElement[0] = this.element(By.xpath(navbar));
			// Realiza la siguiente validación porque dependiendo las opciones activas para
			// el usuario puede cambiar la forma de localizar el elemento del menu
			if (navLinkElement[0] == null) {
				navbar2 = navbar2.replace("{NAVBAR-OPTION}", ControllerFiducia.getMenu(tipoPrueba));
				navLinkElement[0] = this.element(By.xpath(navbar2));
			}
			// Se deben cumplir las condiciones que hacen uso de un submenu
			if (!tipoPrueba.contains("Preventa") || tipoPrueba.contains("Administración")
					|| tipoPrueba.contains("Informes")) {
				submenu = submenu.replace("{NAVBAR-OPTION}", ControllerFiducia.getMenu(tipoPrueba)).replace("{SUBMENU}",
						ControllerFiducia.getSubmenu(tipoPrueba));
				navLinkElement[1] = this.element(By.xpath(submenu));
				// Se deben cumplir las condiciones que hacen uso de un submenu 2
				if ((!tipoPrueba.contains("Informes - Preventa") || !tipoPrueba.contains("Preventa")
						|| !tipoPrueba.contains("Preventa")) && !tipoPrueba.contains("Descarga de formatos")
						&& !tipoPrueba.contains("Pendientes de descarga") && tipoPrueba.contains("Informes")
						&& navLinkElement[1] != null) {
					for (int i = 0; i < navLinkElement.length - 1; i++) {
						DXCUtil.wait(1);
						navLinkElement[i].click();
					}
					submenu2 = submenu2.replace("{SUBMENU}", ControllerFiducia.getSubmenu2(tipoPrueba));
					navLinkElement[2] = this.element(By.xpath(submenu2));
					navLinkElement[0].click();
				}
			}
			// se verificara esta información constantemente para validar que un mensaje que
			// no permite continuar las pruebas aparezca
			msgError = this.msgError();
			if (msgError.contains("Sesión no existe")) {
//				Evidence.save(msgError + " Evidencias " + fechaHoraTx, this);
				Evidence.save(msgError + " Evidencias " + fechaHoraTx);
				return msgError;
			}
		} while (navLinkElement[0] == null && contador <= 2);

		// realiza el ingreso a la información recolectada anteriormente
		if (navLinkElement[0] != null) {
			DXCUtil.wait(1);
			navLinkElement[0].click();
			if (navLinkElement[1] != null) {
				DXCUtil.wait(1);
				navLinkElement[1].click();
				if (navLinkElement[2] != null) {
					DXCUtil.wait(1);
					navLinkElement[2].click();
				}
			}
			msgError = this.msgError();
			if (!msgError.isEmpty()) {
//				Evidence.save(msgError + " Evidencias " + fechaHoraTx, this);
				Evidence.save(msgError + " Evidencias " + fechaHoraTx);
				return msgError;
			}

		} else {
//			Evidence.save(msgError + " Evidencias " + fechaHoraTx, this);
			Evidence.save(msgError + " Evidencias " + fechaHoraTx);
			return "No se ha encontrado la opción " + ControllerFiducia.getMenu(tipoPrueba);
		}
		return "";
	}

	/**
	 * Retornara un String con un texto si el mensaje que no se espera aparece, pero
	 * si no llega a aparecer retornara vacio
	 * 
	 * @return
	 */
	public String msgError() {
		int contador = 0;
		WebElement selectFormatoDesc = null;
		do {
			contador++;
			DXCUtil.wait(1);
			selectFormatoDesc = this.element(PageFiduciaFront.labelSesionExiste);
			if (selectFormatoDesc == null)
				selectFormatoDesc = this.element(PageFiduciaFront.msgErrorUrl);

		} while (selectFormatoDesc == null && contador <= 2);

		contador = 0;
		if (selectFormatoDesc == null)
			do {
				contador++;
				DXCUtil.wait(1);
			} while (contador <= 2);
		else {
			selectFormatoDesc = this.element(PageFiduciaFront.labelSesionExiste);
			if (selectFormatoDesc == null)
				selectFormatoDesc = this.element(PageFiduciaFront.msgErrorUrl);
			return selectFormatoDesc.getText();
		}
		return "";
	}

	// Almacenara mensajes para ser enviados al controller
	public static String[] getMgsInformativo() {
		return msgRes;
	}
}

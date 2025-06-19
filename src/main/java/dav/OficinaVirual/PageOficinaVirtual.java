package dav.OficinaVirual;

import java.sql.Driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import dav.pymes.moduloTx.PageOrigen;
import dav.middlePymes.PageServicios;
import dav.pymes.PageLoginPymes;
import dxc.execution.BaseTestNG;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import dxc.execution.BasePageWeb;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;


public class PageOficinaVirtual extends BasePageWeb {
	By Menu = By.id("mnMenu");
	By dflMenu = By.xpath("//a[@class='Nivel1']"); // Diferencia el menu ya que cambia el locator por mnMenu_1 Nivel1
													// mnMenu_3

	// XPATH QUE REMPLAZAN EL TEXTO CONTENIDO ('NB_MENU_INICIAL','NB_SUBMENU') POR
	// EL QUE DESEAMOS BUSCAR
	String xPathLocMenu = "//a[@class='Nivel1'][contains(text(), 'NB_MENU_INICIAL')]//parent::td";
	String xPathLocSubMenu = "//a[@class='Nivel2'][contains(text(), 'NB_SUBMENU')]//parent::td";

	By TipoSolicitud = By.xpath("//*[@id=\"rn_SelectionInput_2_Incident.CustomFields.CO.FORMULARIO\"]");

	By NumeroCuenta = By.xpath("//*[@id=\"CO_CANCELACION_CUENTAS_NUMERO_PRODUCTO_Field\"]");
	By CuentaAhorros = By.xpath("//*[@id=\"CO_CANCELACION_CUENTAS_TIPO_PRODUCTO_Field\"]/input[1]");
	By TipoAbono = By.xpath("//*[@id=\"CO_CANCELACION_CUENTAS_TIPO_ABONO_Field\"]");
	By VolverConsulta = By.xpath("//*[@id=\"im_MainContent\"]/div/div[2]/div/button");
	By EnviarSolicitud = By.xpath("//*[@id=\"sendForm\"]");
	By CrearSolicitud = By.xpath("//*[@id=\"im_MainContent\"]/div[2]/div[2]/div[1]/button");
	By ContinuarSolicitud = By.id("butonSubmit");
	WebElement Consulta1 = null;
	String TipSolicitud;
	String Numero1Cuenta;
	String TipoAbonos;
	PageOrigen pageOrigen = null;

	public PageOficinaVirtual(BasePageWeb parentPage) {
		super(parentPage);
		// TODO Auto-generated constructor stub
	}
	
	

	/**
	 * El metodo Realiza la creaci�n de una solicitud en la oficina virtual del
	 * Portal PYME
	 * 
	 * @throws Exception
	 */
	public void CrearSolicitud() throws Exception {
		this.TipSolicitud = SettingsRun.getTestData().getParameter("Tipo Solicitud").trim();
		this.Numero1Cuenta = SettingsRun.getTestData().getParameter("cuenta a cancelar").trim();
		this.TipoAbonos = SettingsRun.getTestData().getParameter("Tipo Abono").trim();

		DXCUtil.wait(2);
		Evidence.save("Solicitudes");
		// click para iniciar con la creacion de la solicitud
		this.click(CrearSolicitud);
		Evidence.save("Creacion Solicitud");
		// Seleciona el Tipo de Solicitud
		this.selectListItem(TipoSolicitud, TipSolicitud);
		Evidence.save("Seleccion tipo de Solicitud");
		DXCUtil.wait(2);
		// Continua con el flujo para llevarnos al formulario de la Solicitud
		this.click(ContinuarSolicitud);
		DXCUtil.wait(2);
		Evidence.save("Formulario Cancelacion");
		// Diligencia el formulario con la informacion del Archivo de Data
		this.write(NumeroCuenta, Numero1Cuenta);
		this.click(CuentaAhorros);
		this.selectListItem(TipoAbono, TipoAbonos);
		Evidence.save("Formulario Diligenciado");
		// Envia la solicitud
		this.click(EnviarSolicitud);
		do {
			Consulta1 = this.element(VolverConsulta);
			DXCUtil.wait(2);
		} while (Consulta1 == null);
		Evidence.save("Solicitud Enviada");
		DXCUtil.wait(2);
		// Regresa a la pantalla incial donde se ven la solicitudes
		this.click(VolverConsulta);
		DXCUtil.wait(2);
		Evidence.save("Solicitud Recibida");
		Reporter.reportEvent(Reporter.MIC_PASS, "Solicitud Enviada");
	}

}

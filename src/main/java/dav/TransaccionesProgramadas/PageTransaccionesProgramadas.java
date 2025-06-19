package dav.TransaccionesProgramadas;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PagePortalPymes;
import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageTransaccionesProgramadas extends PagePortalPymes {

	PageOrigen pageOrigen = null;

	String xPathTrx = "(//tr[@class='CeldaGrilla' and contains(., 'NUM_PROD') and contains(., 'REF1')]//center//a[contains(., 'NOM')])";

	public PageTransaccionesProgramadas(BasePageWeb parentPage) {
		super(parentPage);
		// TODO Auto-generated constructor stub
	}

	public String ConsultaPagosAutomatico() throws Exception {
		String reF1 = SettingsRun.getTestData().getParameter("Referencia1 / Tipo Producto Destino").trim();
		String numeroProducto = SettingsRun.getTestData().getParameter("Número producto origen").trim();
		String nombreArecordar = SettingsRun.getTestData().getParameter("Referencia2 / Número Producto Destino").trim();


		this.pageOrigen = new PageOrigen(this);

		String msgError = null;

		String pagoyTx = this.pageOrigen.getTextMid();

		if (pagoyTx.contains("Pagos, Transferencias y otros")) {
			msgError = pageOrigen.irAOpcion("Transacciones Programadas", "Pagos, Transferencias y otros",
					"Transacciones Programadas");
		} else if (pagoyTx.contains("Pagos, Transferencias y Otros")) {
			msgError = pageOrigen.irAOpcion("Transacciones Programadas", "Pagos, Transferencias y Otros",
					"Transacciones Programadas");

		} else {
			msgError = pageOrigen.irAOpcion("Transacciones Programadas", "Pagos y Transferencias",
					"Transacciones Programadas");
		}

		Evidence.saveFullPage("Consulta de tx", this);

		WebElement objRadioButtonProd = null;
		String xPathRB = xPathTrx.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4)).replace("REF1", reF1).replace("NOM", nombreArecordar);
		do {
			objRadioButtonProd = this.element(xPathRB);

			if (objRadioButtonProd != null) {
				// Se encontró el producto, salir del bucle
				break;
			}

			// Buscar el botón de siguiente y verificar si está habilitado
			WebElement btnSiguiente = this.element(By.xpath("//*[@id='cphCuerpo_lnkSiguiente']"));
			if (btnSiguiente != null && !btnSiguiente.getAttribute("class").contains("aspNetDisabled")) {
				btnSiguiente.click();
				// Realizar la búsqueda nuevamente después de hacer clic en el botón de
				// siguiente
				objRadioButtonProd = this.element(xPathRB);
			} else {
				// Si no hay botón de siguiente o está deshabilitado, salir del bucle
				break;
			}
		} while (true);

		if (objRadioButtonProd == null) {
			Evidence.saveFullPage("Error-ProdDestino", this);
			return "Consulta Nombre [" + nombreArecordar + " - Referencia -" + reF1 + " - " + numeroProducto + "] NO encontrado";
		}

		// SI LLEGA A ESTE ES PORQUE EXISTE EL PRODUCTO ORIGEN DE LOS FONDOS
		this.click(objRadioButtonProd);
		Evidence.saveFullPage("Consulta", this);
		
		
		return null;

	}
}

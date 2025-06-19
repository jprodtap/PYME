package dav.Consultas_Y_Extractos;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PagePortalPymes;
import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageSaldos extends PagePortalPymes {

	String tipoConsul, tiempoconsulta;

	By listProduc = By.id("cphCuerpo_ddProducto");
//	By campsaldoTotal = By.id("cphCuerpo_lbCtaAhorroSaldoTotal");
	By campsaldoTotal = By.xpath("//*[@id='cphCuerpo_lbCtaAhorroSaldoTotal' or @id='cphCuerpo_lblSaldoTotal'or @id='cphCuerpo_lbCRESaldo']");
//	By campdisponiblenefectivo = By.id("cphCuerpo_lbCtaAhorroDisponibleEfectivo");
	By campdisponiblenefectivo = By
			.xpath("//*[@id='cphCuerpo_lbCtaAhorroDisponibleEfectivo' or @id='cphCuerpo_lblDispEfectivo']");
//	By campsaldoCanjeLocal = By.id("cphCuerpo_lbCtaAhorroCanjeLocal");
	By campsaldoCanjeLocal = By
			.xpath("//*[@id='cphCuerpo_lbCtaAhorroCanjeLocal' or @id='cphCuerpo_lblSaldoCanjeLocal']");
//	By campsaldoCanjePlaza = By.id("cphCuerpo_lbCtaAhorroCanjePlaza");
	By campsaldoCanjePlaza = By
			.xpath("//*[@id='cphCuerpo_lbCtaAhorroCanjePlaza' or @id='cphCuerpo_lblSaldoCanjeLocal']");
//	By campsaldoCongeladoPorEmbargo = By.id("cphCuerpo_lbCtaAhorroCongeladoEmbargo");
	By campsaldoCongeladoPorEmbargo = By
			.xpath("//*[@id='cphCuerpo_lbCtaAhorroCongeladoEmbargo' or @id='cphCuerpo_lblSaldoCongeladoporEmbargo']");

//	Detalle de Saldo Familia de Fondo
	String tablaFondos = "//td[contains(text(),'NUM')]//following-sibling::td";

	PageOrigen pageOrigen = null;

	public PageSaldos(BasePageWeb parentPage) {
		super(parentPage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Metodo Obtiene los datos del producto del Portal
	 * @param tipoProducto
	 * @param numeroProducto
	 * @return
	 * @throws Exception
	 */
	public String[] ConsultaSaldo(String tipoProducto, String numeroProducto) throws Exception {
		String saldoTotal = null;
		String disponibleEnEfectivo = null;
		String saldoCanjeLocal = null;
		String saldoCanjePlaza = null;
		String saldoCongeladoPorEmbargo = null;
		String producto = SettingsRun.getTestData().getParameter("Producto");
		if (!producto.equals("FAMILIA FONDOS")) {

			String msgError = this.lisProducto(listProduc, tipoProducto, numeroProducto);
//			Evidence.save("Movimientos", this);
			Evidence.save("Movimientos");
			if (msgError != null) {
				String[] msg = { msgError };
				return msg;
			}

			
			this.StandbytimePyme(campsaldoTotal, "Saldo Total", false);
			saldoTotal = this.getText(campsaldoTotal);

			if (!tipoProducto.equals("Leasing")&&!tipoProducto.equals("leasing")&&!tipoProducto.equals("Crediexpress")&&!tipoProducto.equals("crediexpress")) {
				
			this.StandbytimePyme(campdisponiblenefectivo, "Disponible en efectivo", false);
			disponibleEnEfectivo = this.getText(campdisponiblenefectivo);

			this.StandbytimePyme(campsaldoCanjeLocal, "Saldo Canje Local", false);
			saldoCanjeLocal = this.getText(campsaldoCanjeLocal);

			this.StandbytimePyme(campsaldoCanjePlaza, "Saldo Canje Plaza", false);
			saldoCanjePlaza = this.getText(campsaldoCanjePlaza);

			this.StandbytimePyme(campsaldoCongeladoPorEmbargo, "Saldo Congelado Por Embargo", false);
			saldoCongeladoPorEmbargo = this.getText(campsaldoCongeladoPorEmbargo);
			
			}

			String[] detalleSaldos = { saldoTotal, disponibleEnEfectivo, saldoCanjeLocal, saldoCanjeLocal,
					saldoCanjePlaza, saldoCongeladoPorEmbargo };
			
			return detalleSaldos;
		} else {
			DXCUtil.wait(3);
			WebElement Objet =null;
			do {
				String xpath = this.tablaFondos.replace("NUM", DXCUtil.right(numeroProducto, 4));
				Objet = this.element(xpath);
				DXCUtil.wait(1);
			} while (Objet == null);
			String valorTx = Objet.getText();
			String[] detalleSaldos = { valorTx };
			
			return detalleSaldos;
		}

	}



	/**
	 * 
	 * @param locator
	 * @param nameCamp
	 * @param lista    es una lista True o False
	 * @throws Exception
	 */
	public void StandbytimePyme(By locator, String nameCamp, boolean lista) throws Exception {
		int contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
			if (contador > 30) {
				if (lista) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro la lista: " + nameCamp);

				} else {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el campo: " + nameCamp);

				}
				this.pageOrigen = new PageOrigen(this);
				this.pageOrigen.terminarIteracion();
			}
		} while (this.element(locator) == null);
	}

	public String formatearNumero(String numero) throws ParseException {
		try {
			// Elimina caracteres no numúricos y espacios antes de intentar convertir
			String numeroLimpio = numero.replaceAll("[^0-9.,]", "");

			// Reemplaza todas las comas por puntos para manejar el formato correcto
			numeroLimpio = numeroLimpio.replace(',', '.');

			// Verifica si el número ya tiene un formato correcto
			if (numeroLimpio.matches("\\d+\\.\\d+")) {
				return numeroLimpio;
			}

			// Parsea el n�mero limpio
			double parsedNumber = Double.parseDouble(numeroLimpio);

			// Formatea el n�mero y devuelve el resultado
			return String.format(Locale.US, "%.2f", parsedNumber);
		} catch (NumberFormatException e) {
			// Maneja la excepci�n si el número no se puede convertir
			e.printStackTrace();
			throw new ParseException("Error al formatear el número", 0);
		}
	}
}

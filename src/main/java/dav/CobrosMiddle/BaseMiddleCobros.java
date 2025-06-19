package dav.CobrosMiddle;

import dxc.library.reporting.Reporter;

//import dxc.execution.Reporter;

public class BaseMiddleCobros {

	/**
	 * Este es un diccionario para los segmentos de PYME u otros segmentos que
	 * tengan similitud, dependiendo el [destino] que llega a este m�todo retornara
	 * su debido motivo correspondiente, ya sea por una tranferencia o un rechazo.
	 * 
	 * @param motivo
	 * @return motivo
	 */
	public String diccionarioSegmentos(String destino) {

		String motivo = null;

		switch (destino) {

		/*
		 * ****************************** motivos proveedores
		 */

		// MOTIVO 5 Pago Proveedores DAVIVIENDA
		case "Pago a Proveedores CUENTA DE AHORROS":
		case "Pago a Proveedores CUENTA CORRIENTE":
			motivo = "5";
			break;

		// MOTIVO 37 Pago Proveedores Daviplata
		case "Pago a Proveedores DAVIPLATA":
			motivo = "31";
			break;

		// MOTIVO 32 Pago proveedores Tarjeta Prepago Maestro
		case "Pago a Proveedores TARJETA PREPAGO MAESTRO":
			motivo = "32";
			break;

		// MOTIVO 6 Pago Proveedores ACH
		case "Pago a Proveedores CUENTAS AHORRO OTROS BANCOS":
		case "Pago a Proveedores CUENTAS CORRIENTES OTROS BANCOS":
			motivo = "6";
			break;

		/*
		 * ****************************** motivos rechazo proveedores
		 */

		// MOTIVO 33 Pago a Proveedores DAVIPLATA Rechazado
		case "Pago a Proveedores DAVIPLATA Rechazo":
			motivo = "33";
			break;
		// MOTIVO 34 Pago a Proveedores TARJETA PREPAGO MAESTRO Rechazo
		case "Pago a Proveedores TARJETA PREPAGO MAESTRO Rechazo":
			motivo = "34";
			break;

		/*
		 * ****************************** motivos n�mina
		 */

		// MOTIVO 3 Pago de N�minas Davivienda
		case "Pago de Nóminas CUENTA DE AHORROS":
		case "Pago de Nóminas CUENTA CORRIENTE":
			motivo = "3";
			break;

		// MOTIVO 4 Pago de N�minas Otros Bancoss
		case "Pago de Nóminas CUENTAS AHORRO OTROS BANCOS":
		case "Pago de Nóminas CUENTAS CORRIENTES OTROS BANCOS":
			motivo = "4";
			break;

		// MOTIVO 27 Pago de N�minas Daviplata
		case "Pago de Nóminas DAVIPLATA":
			motivo = "27";
			break;

		// MOTIVO 28 Pago de N�minas Tarjeta Prepago Maestro
		case "Pago de Nóminas TARJETA PREPAGO MAESTRO":
			motivo = "28";
			break;
		/*
		 * ****************************** motivos rechazo n�mina
		 */
		// MOTIVO 29 PPago de N�minas DAVIPLATA Rechazado
		case "Pago de Nóminas DAVIPLATA Rechazo":
			motivo = "29";
			break;
		// MOTIVO 30 Pago de N�minas TARJETA PREPAGO MAESTRO Rechazo
		case "Pago de Nóminas TARJETA PREPAGO MAESTRO Rechazo":
			motivo = "30";
			break;

		/*
		 * ****************************** motivos cuenta inscrita, no inscrita y mismo
		 * ****************************** nit
		 */

		// MOTIVO 1 Pago cuenta inscrita, no inscrita y mismo nit DAVIVIENDA
		case "Transferencias Cuenta Inscrita CUENTA DE AHORROS":
		case "Transferencias Cuenta Inscrita CUENTA CORRIENTE":
		case "Transferencias Cuenta No Inscrita CUENTA DE AHORROS":
		case "Transferencias Cuenta No Inscrita CUENTA CORRIENTE":
		case "Transferencias Mismo NIT CUENTA DE AHORROS":
		case "Transferencias Mismo NIT CUENTA CORRIENTE":
		case "Transferencia NIT Propio CUENTA DE AHORROS":
		case "Transferencia NIT Propio CUENTA CORRIENTE":
			motivo = "1";
			break;

		// MOTIVO 2 Pago cuenta inscrita, no inscrita y mismo nit Otros Bancos
		case "Transferencias Cuenta Inscrita CUENTAS AHORRO OTROS BANCOS":
		case "Transferencias Cuenta Inscrita CUENTAS CORRIENTES OTROS BANCOS":
		case "Transferencias Cuenta No CUENTAS AHORRO OTROS BANCOS":
		case "Transferencias Cuenta No Inscrita CUENTAS CORRIENTES OTROS BANCOS":

			motivo = "2";
			break;

		/*
		 * ****************************** motivos pago de servicios
		 */
		// MOTIVO 1 Pago Proveedores DAVIVIENDA
		case "Pago de Servicios":
			motivo = "7";
			break;
		/*
		 * ****************************** motivos Dep�sitos electr�nicos, aplica para la
		 * ****************************** mayoria
		 */
		// MOTIVO 44 DEPOSITOS ELECTRONICOS OTRAS ENTIDA
		case "Transferencias Cuenta Inscrita DEPÓSITOS ELECTRONICOS":
		case "Transferencias Cuenta No Inscrita DEPÓSITOS ELECTRONICOS":
		case "Pago de NÓminas DEPÓSITOS ELECTRONICOS":
		case "Pago a Proveedores DEPÓSITOS ELECTRONICOS":
			motivo = "44";
			break;
		}
		return motivo;

	}

	/**
	 * Se retornara el [tipo destino] al cual corresponde el [destino] al que se le
	 * realizo algun cobro
	 * 
	 * @param tipoDestino
	 * @return
	 */
	public String tipoDestino(String destino) {

		String tipoDestino = null;

		switch (destino) {
		case "CUENTA DE AHORROS":
		case "CUENTA CORRIENTE":
			tipoDestino = "Davivienda";
			break;

		case "CUENTAS AHORRO OTROS BANCOS":
		case "CUENTAS CORRIENTES OTROS BANCOS":
			tipoDestino = "Otros Bancos";
			break;

		case "DEPÓSITOS ELECTRONICOS":
			tipoDestino = "Depósitos Electrónicos";
			break;

		case "TARJETA PREPAGO MAESTRO":
			tipoDestino = "Tarjeta Prepago Maestro";
			break;

		case "DAVIPLATA":
			tipoDestino = "Daviplata";
			break;

		case "Pago de Servicios":
			tipoDestino = "Pago de Servicios";
			break;

		default:
			tipoDestino = "No se entontro información relacionada";
			break;
		}

		return tipoDestino;

	}

	/**
	 * Realiza el descuento por el tipo destino si la transacci�n es rechazada
	 * 
	 * @param descuentoXRechazo
	 * @param valorCobroRechazo
	 * @param descuentoXTxRechazo
	 * @param resultado
	 * @param i                   - recibe un indice
	 * @throws Exception
	 */
	public double descuentoXRechazo(double descuentoXRechazo, double valorCobroRechazo, double descuentoXTxRechazo) throws Exception {
		double resultado = 0;
		descuentoXRechazo = valorCobroRechazo * descuentoXTxRechazo;
		descuentoXRechazo = valorCobroRechazo - descuentoXRechazo;
		resultado = descuentoXRechazo;
		Reporter.reportEvent(Reporter.MIC_INFO,
				"Se realiza el decuento del " + descuentoXTxRechazo * 100
						+ "% sobre el valor de rechazo de transacción " + valorCobroRechazo
						+ ", total cobro con descuento: " + descuentoXRechazo);
		return resultado;
	}

	/**
	 * Si el indicador es Si, y el si el saldo del mes anterior se encuentra fuera
	 * del rango, se incluira como si estuviera dentro del rango
	 * 
	 * @param aplicaCombo
	 * @param saldoPromedioMesAnt
	 * @param topeInferiorSaldo
	 * @param topeSuperiosSaldo
	 * @param descuentoXTx
	 * @param valorCobro
	 * @param descuentoDentroRango
	 * @param descuentoSobreRango
	 * @param resultado
	 * @param i
	 * @throws Exception
	 */
	public double descuentoIndicadorSConCombo(String aplicaCombo, double saldoPromedioMesAnt, double topeInferiorSaldo,
			double topeSuperiosSaldo, double descuentoXTx, double valorCobro, double descuentoDentroRango,
			double descuentoSobreRango) throws Exception {
		double resultado = 0;

		// Como aplica combo es "SI" y el indicador de cumplimiento es "S", el saldo
		// fuera de rango queda incluido dentro del tope inferior y superios
		if (aplicaCombo.equals("SI")) {
			// Como aplica combo es "SI" y el indicador de cumplimiento es "S", el saldo
			// fuera de rango queda incluido dentro del tope inferior y superior
			if (saldoPromedioMesAnt < topeInferiorSaldo
					|| saldoPromedioMesAnt > topeInferiorSaldo && saldoPromedioMesAnt < topeSuperiosSaldo) {
				// el saldo promedio aplicara como si estuviera dentro del rango y aplicara
				// descuento al cobro porque el indicador de cumplimiento es "S"
				descuentoXTx = valorCobro * descuentoDentroRango;
				descuentoXTx = valorCobro - descuentoXTx;
				resultado = descuentoXTx;
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se realiza el decuento del " + descuentoDentroRango * 100
								+ "% sobre el valor del cobro de transacción " + valorCobro
								+ ", total cobro con descuento: " + descuentoXTx);
			} else if (saldoPromedioMesAnt > topeSuperiosSaldo) {
				descuentoXTx = valorCobro * descuentoSobreRango;
				descuentoXTx = valorCobro - descuentoXTx;
				resultado = descuentoXTx;
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se realiza el decuento del " + descuentoSobreRango * 100
								+ "% sobre el valor del cobro de transacción " + valorCobro
								+ ", total cobro con descuento: " + descuentoXTx);

			}
		} else if (aplicaCombo.equals("NO")) {
			// el descuento no se encuentra habilitado, por lo cual se cobrara la tarifa
			// completa del
			resultado = valorCobro;
		}
		return resultado;
	}

	/**
	 * Si el indicador es No, y el si el saldo del mes anterior se encuentra fuera
	 * del rango, no se incluira dentro del rango.
	 * 
	 * @param aplicaCombo
	 * @param saldoPromedioMesAnt
	 * @param topeInferiorSaldo
	 * @param topeSuperiosSaldo
	 * @param valorCobro
	 * @param descuentoXTx
	 * @param descuentoSobreRango
	 * @param descuentoDentroRango
	 * @param resultado
	 * @param i
	 * @throws Exception
	 */
	public double descuentoIndicadorNConCombo(String aplicaCombo, double saldoPromedioMesAnt, double topeInferiorSaldo,
			double topeSuperiosSaldo, double valorCobro, double descuentoXTx, double descuentoSobreRango,
			double descuentoDentroRango) throws Exception {
		double resultado = 0;
		if (aplicaCombo.equals("SI")) {
			if (saldoPromedioMesAnt < topeInferiorSaldo) {
				// el saldo promedio no aplicara si estuviera dentro del rango, cobrando la
				// tarifa completa
				resultado = valorCobro;
			} else if (saldoPromedioMesAnt > topeSuperiosSaldo) {
				// El saldo promedio aplicara descuento si se encuentra sobre el rango
				descuentoXTx = valorCobro * descuentoSobreRango;
				descuentoXTx = valorCobro - descuentoXTx;
				resultado = descuentoXTx;
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se realiza el decuento del " + descuentoSobreRango * 100
								+ "% sobre el valor del cobro de transacción " + valorCobro
								+ ", total cobro con descuento: " + descuentoXTx);
			} else if (saldoPromedioMesAnt > topeInferiorSaldo && saldoPromedioMesAnt < topeSuperiosSaldo) {
				// el saldo promedio aplicara como si estuviera dentro del rango y aplicara
				// descuento al cobro porque el indicador de cumplimiento es "S"
				descuentoXTx = valorCobro * descuentoDentroRango;
				descuentoXTx = valorCobro - descuentoXTx;
				resultado = descuentoXTx;
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se realiza el decuento del " + descuentoDentroRango * 100
								+ "% sobre el valor del cobro de transacción " + valorCobro
								+ ", total cobro con descuento: " + descuentoXTx);
			}
		} else if (aplicaCombo.equals("NO")) {
			// la tarifa del cobro no contara con descuento se cobrara completa
			resultado = valorCobro;
		}

		return resultado;
	}

	/**
	 * Si el indicador es Si, y el si el saldo del mes anterior se encuentra fuera
	 * del rango, se incluira como si estuviera dentro del rango
	 * 
	 * @param saldoPromedioMesAnt
	 * @param topeInferiorSaldo
	 * @param topeSuperiosSaldo
	 * @param descuentoXTx
	 * @param valorCobro
	 * @param descuentoDentroRango
	 * @param descuentoSobreRango
	 * @param resultado
	 * @param i
	 * @throws Exception
	 */
	public double descuentoIndicadorSSinCombo(double saldoPromedioMesAnt, double topeInferiorSaldo,
			double topeSuperiosSaldo, double descuentoXTx, double valorCobro, double descuentoDentroRango,
			double descuentoSobreRango) throws Exception {
		double resultado = 0;
		// valida la informaci�n con el indicador en S
		// Como aplica combo es "SI" y el indicador de cumplimiento es "S", el saldo
		// fuera de rango queda incluido dentro del tope inferior y superior
		if (saldoPromedioMesAnt < topeInferiorSaldo
				|| saldoPromedioMesAnt > topeInferiorSaldo && saldoPromedioMesAnt < topeSuperiosSaldo) {
			// el saldo promedio aplicara como si estuviera dentro del rango y aplicara
			// descuento al cobro porque el indicador de cumplimiento es "S"
			descuentoXTx = valorCobro * descuentoDentroRango;
			descuentoXTx = valorCobro - descuentoXTx;
			resultado = descuentoXTx;
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Se realiza el decuento del " + descuentoDentroRango * 100
							+ "% sobre el valor del cobro de transacción " + valorCobro
							+ ", total cobro con descuento: [ " + descuentoXTx + " ].");
			resultado = descuentoXTx;
		} else if (saldoPromedioMesAnt > topeSuperiosSaldo) {
			descuentoXTx = valorCobro * descuentoSobreRango;
			descuentoXTx = valorCobro - descuentoXTx;
			resultado = descuentoXTx;
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Se realiza el decuento del [ " + descuentoSobreRango * 100
							+ "% ] sobre el valor del cobro de transacción [ " + valorCobro
							+ " ], total cobro con descuento: [ " + descuentoXTx + " ].");
		}

		return resultado;
	}

	/**
	 * Si el indicador es No, y el si el saldo del mes anterior se encuentra fuera
	 * del rango, no se incluira dentro del rango.
	 * 
	 * @param saldoPromedioMesAnt
	 * @param topeInferiorSaldo
	 * @param topeSuperiosSaldo
	 * @param descuentoXTx
	 * @param valorCobro
	 * @param descuentoDentroRango
	 * @param descuentoSobreRango
	 * @param resultado
	 * @param i
	 * @throws Exception
	 */
	public double descuentoIndicadorNSinCombo(double saldoPromedioMesAnt, double topeInferiorSaldo,
			double topeSuperiosSaldo, double descuentoXTx, double valorCobro, double descuentoDentroRango,
			double descuentoSobreRango) throws Exception {
		double resultado = 0;
		// valida la informaci�n con el indicador en N
		if (saldoPromedioMesAnt < topeInferiorSaldo) {
			// el saldo promedio no aplicara si estuviera dentro del rango, cobrando la
			// tarifa completa
			resultado = valorCobro;
		} else if (saldoPromedioMesAnt > topeSuperiosSaldo) {
			// El saldo promedio aplicara descuento si se encuentra sobre el rango
			descuentoXTx = valorCobro * descuentoSobreRango;
			descuentoXTx = valorCobro - descuentoXTx;
			resultado = descuentoXTx;
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Se realiza el decuento del [" + descuentoSobreRango * 100
							+ "% ] sobre el valor del cobro de transacción [ " + valorCobro
							+ " ], total cobro con descuento: [ " + descuentoXTx + " ].");
		} else if (saldoPromedioMesAnt > topeInferiorSaldo && saldoPromedioMesAnt < topeSuperiosSaldo) {
			// el saldo promedio aplicara como si estuviera dentro del rango y aplicara
			// descuento al cobro porque el indicador de cumplimiento es "S"
			descuentoXTx = valorCobro * descuentoDentroRango;
			descuentoXTx = valorCobro - descuentoXTx;
			resultado = descuentoXTx;
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Se realiza el decuento del [ " + descuentoDentroRango * 100
							+ "% ] sobre el valor del cobro de transacción [" + valorCobro
							+ " ], total cobro con descuento: [ " + descuentoXTx + " ].");
		}

		return resultado;

	}

}

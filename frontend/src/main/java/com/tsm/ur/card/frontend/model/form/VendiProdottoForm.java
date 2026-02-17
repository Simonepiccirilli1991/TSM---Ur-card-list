package com.tsm.ur.card.frontend.model.form;

import lombok.Data;

@Data
public class VendiProdottoForm {
    private String idProdotto;
    private String tipoProdotto;
    private Double prezzoVendita;
    private Double costiVendita;
    private String dataVendita;
    private String piattaformaVendita;
    private String note;
}


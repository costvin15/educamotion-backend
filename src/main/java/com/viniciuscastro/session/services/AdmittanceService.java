package com.viniciuscastro.session.services;

import com.viniciuscastro.session.clients.AdmittanceFirestoreResource;
import com.viniciuscastro.session.dto.requests.SubmitAdmittanceRequest;
import com.viniciuscastro.session.dto.responses.AdmittanceResponse;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class AdmittanceService {
    @Inject
    AdmittanceFirestoreResource admittanceFirestoreResource;

    public Uni<AdmittanceResponse> registerAdmittance(SubmitAdmittanceRequest request) {
        return Uni.createFrom().item(this.admittanceFirestoreResource.registerAdmittance(request.getSessionCode()))
            .onItem().transformToUni(admittance -> {
                return Uni.createFrom().item(new AdmittanceResponse(
                    admittance.getId(),
                    admittance.getUserId(),
                    admittance.getSessionId(),
                    admittance.getCreatedAt().toDate()
                ));
            });
    }
}

package lk.slife.policyreportexporter.repository.mariadb.projection;

import org.hibernate.annotations.Imported;

import java.time.Instant;

@Imported
public record ProposalView(String proposalNo, Instant sysDate) {
}

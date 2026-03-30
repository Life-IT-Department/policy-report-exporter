package lk.slife.policyreportexporter.entity.mariadb;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "master_proposal")
public class MasterProposalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "proposal_no", length = 55)
    private String proposalNo;

    @Column(name = "proposal_holder", length = 10000)
    private String proposalHolder;

    @Column(name = "agent", length = 55)
    private String agent;

    @Column(name = "last_sync")
    private Instant lastSync;

    @Column(name = "success_rate")
    private Float successRate;

    @Column(name = "process_status")
    private Integer processStatus;

    @Column(name = "status_epos")
    private Integer statusEpos;

    @Column(name = "status_process_underwriting")
    private Integer statusProcessUnderwriting;

    @Column(name = "status_peocess_action")
    private Integer statusPeocessAction;

    @Column(name = "sum_at_risk")
    private Double sumAtRisk;

    @Column(name = "proposal_date")
    private Instant proposalDate;

    @Column(name = "branch", length = 55)
    private String branch;

    @Column(name = "sys_date")
    private Instant sysDate;

    @Column(name = "pin")
    private Integer pin;

    @Column(name = "payfre", length = 11)
    private String payfre;

    @Column(name = "polterm", length = 55)
    private String polterm;

    @Column(name = "anlpre")
    private Double anlpre;

    @Column(name = "pdfpin")
    private String pdfpin;

    @Column(name = "epos_ims_transedate")
    private LocalDate eposImsTransedate;

    @Column(name = "mobile", length = 55)
    private String mobile;

    @Column(name = "id_card", length = 20)
    private String idCard;

    @Column(name = "status_healthportal_toLab")
    private Integer statusHealthportalTolab;

    @Column(name = "myarenadocdate")
    private Instant myarenadocdate;

    @Column(name = "digital_sign")
    private Short digitalSign;

    @Column(name = "agentappointmentdate")
    private Instant agentappointmentdate;

    @Column(name = "agentTabStatus")
    private Integer agentTabStatus;

    @Column(name = "new_sign_pin", length = 45)
    private String newSignPin;

    @Column(name = "policy_no", length = 45)
    private String policyNo;

    @ColumnDefault("0")
    @Column(name = "process_status1")
    private Integer processStatus1;

    @ColumnDefault("0")
    @Column(name = "botprocess_status")
    private Integer botprocessStatus;

    @Column(name = "process_start_time")
    private Instant processStartTime;

    @Column(name = "process_end_time")
    private Instant processEndTime;

    @Column(name = "cutoff_status")
    private Integer cutoffStatus;

    @Column(name = "print_pdf_status")
    private Integer printPdfStatus;

    @Column(name = "branch_uw_status")
    private Integer branchUwStatus;

    @Column(name = "branch_code", length = 45)
    private String branchCode;

    @Column(name = "mobile_number_validity_status")
    private Integer mobileNumberValidityStatus;

    @Column(name = "agent_nic", length = 45)
    private String agentNic;

    @Column(name = "agent_mobile", length = 45)
    private String agentMobile;

    @Column(name = "agent_tab_status")
    private Integer agentTabStatus1;

    @Column(name = "status_healthportal_to_lab")
    private Integer statusHealthportalToLab;

    @Column(name = "language", length = 225)
    private String language;

    @Column(name = "e_policyNo", length = 25)
    private String ePolicyno;

    @ColumnDefault("0")
    @Column(name = "e_policy_email")
    private Boolean ePolicyEmail;

    @Column(name = "e_policy_email_status", length = 55)
    private String ePolicyEmailStatus;

    @ColumnDefault("0")
    @Column(name = "e_policy_sms")
    private Boolean ePolicySms;

    @Column(name = "e_policy_sms_status", length = 55)
    private String ePolicySmsStatus;

    @Column(name = "e_policy_delivery_Type", length = 10)
    private String ePolicyDeliveryType;

    @Column(name = "e_policy_OTP", length = 6)
    private String ePolicyOtp;

    @Column(name = "lastAccessTime")
    private Instant lastAccessTime;

    @ColumnDefault("0")
    @Column(name = "e_policy_Agreement", nullable = false)
    private Boolean ePolicyAgreement;

    @Column(name = "alap_delivery_type", length = 50)
    private String alapDeliveryType;

    @ColumnDefault("0")
    @Column(name = "alap_signature_status")
    private Integer alapSignatureStatus;

    @Column(name = "expiry", length = 45)
    private String expiry;

    @ColumnDefault("0")
    @Column(name = "business_introducer_id", nullable = false)
    private Integer businessIntroducerId;

    @Column(name = "lead_serial_no", length = 100)
    private String leadSerialNo;


}
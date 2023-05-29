package ucb.judge.ujjudge.dao

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "submission")
class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    var submissionId: Long = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id")
    var language: Language? = null;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "verdict_type_id")
    var verdictType: VerdictType? = null;

    @Column(name = "submission_date")
    var submissionDate: Timestamp? = null;

    @Column(name = "status")
    var status: Boolean = true;

}
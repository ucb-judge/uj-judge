package ucb.judge.ujjudge.dao

import javax.persistence.*

@Entity
@Table(name = "problem")
class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    var problemId: Long = 0;

    @Column(name = "title")
    var title: String = "";

    @Column(name = "max_time")
    var maxTime: Double = 0.0;

    @Column(name = "max_memory")
    var maxMemory: Int = 0;

    @Column(name = "is_public")
    var isPublic: Boolean = true;

    @Column(name = "status")
    var status: Boolean = true;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "problem")
    var testcases: List<Testcase>? = null;
}
import java.awt.Color
import java.awt.Font
import javax.swing.*

class HomePage : JFrame() {

    private var btnConnect: JButton = JButton()
    private var btnStart: JButton = JButton()
    private var btnStop: JButton = JButton()
    private var cmbPort: JComboBox<String> = JComboBox()
    private var jLabel1: JLabel = JLabel()
    private var jLabel2: JLabel = JLabel()
    private var jLabel3: JLabel = JLabel()
    private var jLabel4: JLabel = JLabel()
    private var jLabel5: JLabel = JLabel()
    private var jLabel6: JLabel = JLabel()
    private var jSeparator1: JSeparator = JSeparator()
    private var lblLastValue: JLabel = JLabel()
    private var lblSampleNumber: JLabel = JLabel()
    private var lblStartTime: JLabel = JLabel()
    private var txtComment: JTextField = JTextField()
    private var txtOperator: JTextField = JTextField()

    init {
        title = "Home Page"
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        initComponents()
    }

    private fun initComponents() {
        jLabel1.text = "Opérateur :"
        jLabel2.text = "Commentaire :"
        btnConnect.text = "Connexion"
        jLabel3.text = "Port :"
        jLabel4.text = "Démarrage :"
        jLabel5.text = "# échantillons :"
        jLabel6.text = "Dernière valeur :"
        btnStart.background = Color(204, 255, 153)
        btnStart.font = Font("Segoe UI", 0, 18) // NOI18N
        btnStart.text = "Start"
        btnStop.background = Color(255, 102, 102)
        btnStop.font = Font("Segoe UI", 0, 18) // NOI18N
        btnStop.setText("Stop")
        lblStartTime.setText("xx:xx:xx")
        lblSampleNumber.setFont(Font("Segoe UI", 0, 14)) // NOI18N
        lblSampleNumber.setText("XXX")
        lblLastValue.setFont(Font("Segoe UI", 0, 24)) // NOI18N
        lblLastValue.setText("XXXXX")
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(jSeparator1)
                                .addGroup(
                                    GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGroup(
                                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                    layout.createSequentialGroup()
                                                        .addComponent(jLabel1)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(
                                                            txtOperator,
                                                            GroupLayout.DEFAULT_SIZE,
                                                            183,
                                                            Short.MAX_VALUE.toInt()
                                                        )
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(jLabel3)
                                                )
                                                .addGroup(
                                                    layout.createSequentialGroup()
                                                        .addComponent(jLabel2)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(txtComment)
                                                )
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(
                                                    btnConnect, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                                    Short.MAX_VALUE.toInt()
                                                )
                                                .addComponent(
                                                    cmbPort,
                                                    0,
                                                    GroupLayout.DEFAULT_SIZE,
                                                    Short.MAX_VALUE.toInt()
                                                )
                                        )
                                )
                                .addGroup(
                                    GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGroup(
                                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                    layout.createSequentialGroup()
                                                        .addComponent(jLabel6)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(lblLastValue)
                                                )
                                                .addGroup(
                                                    layout.createSequentialGroup()
                                                        .addGroup(
                                                            layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                .addComponent(jLabel4)
                                                                .addComponent(jLabel5)
                                                        )
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(
                                                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(lblStartTime)
                                                                .addComponent(lblSampleNumber)
                                                        )
                                                )
                                        )
                                        .addPreferredGap(
                                            LayoutStyle.ComponentPlacement.RELATED,
                                            GroupLayout.DEFAULT_SIZE,
                                            Short.MAX_VALUE.toInt()
                                        )
                                        .addGroup(
                                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(btnStart, GroupLayout.Alignment.TRAILING)
                                                .addComponent(btnStop, GroupLayout.Alignment.TRAILING)
                                        )
                                )
                        )
                        .addContainerGap()
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(
                                    txtOperator,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(
                                    cmbPort,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(jLabel3)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(
                                    txtComment,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(btnConnect)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(
                                    layout.createSequentialGroup()
                                        .addComponent(
                                            btnStart,
                                            GroupLayout.PREFERRED_SIZE,
                                            52,
                                            GroupLayout.PREFERRED_SIZE
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(
                                                    btnStop,
                                                    GroupLayout.PREFERRED_SIZE,
                                                    52,
                                                    GroupLayout.PREFERRED_SIZE
                                                )
                                                .addComponent(jLabel6)
                                                .addComponent(lblLastValue)
                                        )
                                )
                                .addGroup(
                                    layout.createSequentialGroup()
                                        .addGroup(
                                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel4)
                                                .addComponent(lblStartTime)
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(
                                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel5)
                                                .addComponent(lblSampleNumber)
                                        )
                                )
                        )
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())
                )
        )
        pack()
    }
}
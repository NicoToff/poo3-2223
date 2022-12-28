import com.fazecast.jSerialComm.SerialPort
import serialcom.PortSearch
import serialcom.Reader
import java.awt.Color
import java.awt.Font
import java.awt.event.ActionEvent
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.Document

class HomePage(
    var availablePorts: Array<SerialPort>
) : JFrame() {
    private fun Document.addDocumentListener() {
        this.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) {
                update()
            }

            override fun removeUpdate(e: DocumentEvent) {
                update()
            }

            override fun changedUpdate(e: DocumentEvent) {
                update()
            }

            private fun update() {
                if (txtOperator.text.length > 5 && cmbPort.selectedItem != null) {
                    btnConnect.isEnabled = true
                    btnConnect.toolTipText = null
                } else {
                    btnConnect.isEnabled = false
                    btnConnect.toolTipText = toolTipTextBtnConnect
                }
            }
        })
    }

    private fun connectButtonClicked(e: ActionEvent) {
        if (cmbPort.selectedIndex != -1) {
            val port = availablePorts[cmbPort.selectedIndex]
            reader = Reader(this, port)
            btnStart.isEnabled = port.isOpen
            btnConnect.isEnabled = !port.isOpen
            cmbPort.isEnabled = !port.isOpen
        }
    }

    private fun startButtonClicked(e: ActionEvent) {
        reader?.start()
        lblStartTime.text = LocalDateTime.now().toString()
        btnStart.isEnabled = false
        btnStop.isEnabled = true
    }

    private fun stopButtonClicked(e: ActionEvent) {
        reader?.let { saveFile(it) }
        reader?.interrupt()
        btnStop.isEnabled = false
        btnConnect.isEnabled = true
        cmbPort.isEnabled = true
        availablePorts = emptyArray()
        cmbPort.removeAllItems()
        PortSearch(this).start()
    }

    private fun saveFile(reader: Reader) {
        val data = reader.history
        val operator = txtOperator.text
        val port = cmbPort.selectedItem?.toString()
        val startTime = LocalDateTime.parse(lblStartTime.text).toString().replace(":", "")
        val endTime = LocalDateTime.now().toString()
        val folder = File("data")
        if (!folder.exists()) folder.mkdir()
        println(startTime)
        val fileName = "$startTime-$operator".replace(Regex("[\\\\/:)=(*!?\"<>|]"), "")
        val file = File("data/$fileName.csv")
        file.createNewFile()
        FileOutputStream(file).use { stream ->
            for ((time, value) in data.entries) {
                val sanitizedTime = time.toString().replace(Regex("[=!&*^%$#@`~]"), "")
                val entry = "$sanitizedTime;$value\n"
                stream.write(entry.toByteArray())
            }
        }
    }

    // region Swing components
    val cmbPort: JComboBox<String> = JComboBox(availablePorts.map { it.systemPortName }.toTypedArray())
    private var reader: Reader? = null
    val lblLastValue: JLabel = JLabel()
    val lblSampleNumber: JLabel = JLabel()

    private val btnConnect: JButton = JButton()
    private val btnStart: JButton = JButton()
    private val btnStop: JButton = JButton()
    private val lblStartTime: JLabel = JLabel()
    private val txtComment: JTextField = JTextField()
    private val txtOperator: JTextField = JTextField()
    private val toolTipTextBtnConnect = "Veuillez entrer un nom d'opérateur"

    // region Unimportant labels
    private val jLabel1: JLabel = JLabel()
    private val jLabel2: JLabel = JLabel()
    private val jLabel3: JLabel = JLabel()
    private val jLabel4: JLabel = JLabel()
    private val jLabel5: JLabel = JLabel()
    private val jLabel6: JLabel = JLabel()
    private val jSeparator1: JSeparator = JSeparator()
    // endregion
    // endregion

    init {
        title = "Home Page"
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        initComponents()
        btnConnect.addActionListener(::connectButtonClicked)
        btnStart.addActionListener(::startButtonClicked)
        btnStop.addActionListener(::stopButtonClicked)
        txtOperator.document.addDocumentListener()
        PortSearch(this).start()
    }

    private fun initComponents() {
        jLabel1.text = "Opérateur :"
        jLabel2.text = "Commentaire :"
        txtOperator.toolTipText = "Nom de l'opérateur"
        btnConnect.text = "Connexion"
        btnConnect.isEnabled = false
        btnConnect.toolTipText = toolTipTextBtnConnect
        jLabel3.text = "Port :"
        jLabel4.text = "Démarrage :"
        jLabel5.text = "# échantillons :"
        jLabel6.text = "Dernière valeur :"
        btnStart.background = Color(204, 255, 153)
        btnStart.font = Font("Segoe UI", 0, 18) // NOI18N
        btnStart.text = "Start"
        btnStart.isEnabled = false
        btnStop.background = Color(255, 102, 102)
        btnStop.font = Font("Segoe UI", 0, 18) // NOI18N
        btnStop.text = "Stop"
        btnStop.isEnabled = false
        lblStartTime.text = "..."
        lblSampleNumber.font = Font("Segoe UI", 0, 14) // NOI18N
        lblSampleNumber.text = "..."
        lblLastValue.font = Font("Segoe UI", 0, 24) // NOI18N
        lblLastValue.text = "..."
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

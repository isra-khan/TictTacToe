
package javaapplication35;
import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;


public class NewJFrame extends javax.swing.JFrame {

    ArrayList<AudInputLine> lines=new ArrayList<>();
    TargetDataLine inputline;
    File audoutput;
    boolean start=false;
    AudioFormat format;
    Mixer.Info[] mixerInfo;
    AudioInputStream ais;
    String filename;
    AudioFileFormat.Type fileformat;
    String[] exts={"wav","au","aiff","aifc","snd"};
    File directory;
    
    public NewJFrame() {
        
        initComponents();
        filename=System.getProperty("user.home")+File.separator+"AudioRecorder";
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        RefreshInputs();
        File fold=new File(filename);
        if(!fold.exists())
            fold.mkdir();
        directory=new File(fold.getPath());
        filename+=File.separator+"rec";
        startRec.start();
    }
    
//__________ Refresh Input sources_____________
public void RefreshInputs()
{
lines.clear();
mixerInfo = AudioSystem.getMixerInfo();
Line.Info[] targlines;
//getting all TargetLines from all available mixers
for(Mixer.Info m:mixerInfo)
    {
    targlines=AudioSystem.getMixer(m).getTargetLineInfo();
    for(Line.Info ln:targlines)
        {
        AudInputLine tail=new AudInputLine();
        tail.lineInfo=ln;
        tail.mixer=AudioSystem.getMixer(m);
        tail.name=tail.mixer.getMixerInfo().toString();
        lines.add(tail);
        }
    }
//removing TargetLines that do not support any AudioFormat
for(int i=0;i<lines.size();i++)
    {
    try{
        if(((DataLine.Info)lines.get(i).lineInfo).getFormats().length<1)
            {
            lines.remove(i);
            i-=1;
            }
       }catch(Exception exx)
        {
            lines.remove(i);
            i-=1;
        }
    }

cmb_targetdatalines.removeAllItems();
for(AudInputLine dinf:lines)
    cmb_targetdatalines.addItem(dinf);
}

//__________ Refresh audioformats_____________
public void RefreshAudioFormats()
{
int[] bits={24,16,8};
float[] sampling={8000,11025,16000,22050,44100,48000,96000,192000};
AudInputLine taud=((AudInputLine)cmb_targetdatalines.getSelectedItem());

//populating samplerates combobox
cmb_sample.removeAllItems();
for(int i=0;i<sampling.length;i++)
    {
    AudioFormat aftemp=new AudioFormat(sampling[i],8,1,false,true);
    if(taud.lineInfo instanceof DataLine.Info&&((DataLine.Info)taud.lineInfo).isFormatSupported(aftemp)==true)
        {
        cmb_sample.addItem(Float.toString(sampling[i]));
        if(sampling[i]==44100||sampling[i]==48000)
            cmb_sample.setSelectedIndex(i);
        }
    }

//populating sampleBItSize combobox
cmb_bits.removeAllItems();
for(int i=0;i<bits.length;i++)
    {
    AudioFormat aftemp=new AudioFormat(8000,bits[i],1,!(bits[i]==8),true);
    if(taud.lineInfo instanceof DataLine.Info&&((DataLine.Info)taud.lineInfo).isFormatSupported(aftemp)==true)
        cmb_bits.addItem(Integer.toString(bits[i]));
    }

//Populating Channels combobox (mono/stereo)
AudioFormat aftemp=new AudioFormat(8000,8,2,false,true);
cmb_monoORStereo.removeAllItems();
if(taud.lineInfo instanceof DataLine.Info&&((DataLine.Info)taud.lineInfo).isFormatSupported(aftemp)==true)
    cmb_monoORStereo.addItem("Stereo");
cmb_monoORStereo.addItem("Mono");
}

//______________Record________________
public void record(){
try{
start=false;
inputline.open(format);
inputline.start();
ais=new AudioInputStream(inputline);
AudioSystem.write(ais,fileformat, audoutput);
}catch(Exception ex)
    {   
    JOptionPane.showMessageDialog(this, ex.getMessage());
    buttonsEnable(true);
    }

}

//method to handle enabling and disabling UI element when start/stop button are pressed
public void buttonsEnable(boolean f)
{
cmb_targetdatalines.setEnabled(f);
cmb_bits.setEnabled(f);
cmb_file_format.setEnabled(f);
cmb_monoORStereo.setEnabled(f);
cmb_sample.setEnabled(f);
btn_stop.setEnabled(!f);
btn_start.setEnabled(f);
}

//The recording thread. Set apart from EDT
Thread startRec=new Thread()
{
  public void run()
  {
  while(true)
    {
    while(start==false)
        try {
            Thread.sleep(300);
        } catch (Exception ex) {}
    record();
    }
 }
};

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_stop = new javax.swing.JButton();
        btn_start = new javax.swing.JButton();
        cmb_targetdatalines = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        cmb_file_format = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        btn_refresh = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmb_sample = new javax.swing.JComboBox();
        cmb_bits = new javax.swing.JComboBox();
        cmb_monoORStereo = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        btn_stop.setText("Stop");
        btn_stop.setEnabled(false);
        btn_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_stopActionPerformed(evt);
            }
        });

        btn_start.setText("Start");
        btn_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_startActionPerformed(evt);
            }
        });

        cmb_targetdatalines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_targetdatalinesActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel6.setText("Sample Size in bits");

        cmb_file_format.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "WAVE", "AU", "AIFF", "AIFF-C", "SND" }));

        jLabel7.setText("Select Input Source");

        btn_refresh.setText("Refresh Inputs");
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });

        jLabel2.setText("Sample Rate");

        jLabel1.setText("Mono/Stereo");

        jLabel3.setText("File Format");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmb_targetdatalines, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cmb_sample, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmb_bits, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btn_start, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btn_stop, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmb_monoORStereo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                    .addComponent(cmb_file_format, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmb_targetdatalines, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_refresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmb_sample, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmb_bits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmb_monoORStereo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmb_file_format, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_stop, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(btn_start, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_stopActionPerformed
inputline.stop();  
inputline.close();
try {
    Desktop.getDesktop().open(directory);
    } catch (Exception ex) {}
buttonsEnable(true);
    }//GEN-LAST:event_btn_stopActionPerformed

    private void btn_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_startActionPerformed
Date no=new Date();
audoutput=new File(filename+no.toString().replaceAll(":","-")+"."+exts[cmb_file_format.getSelectedIndex()]);
switch(cmb_file_format.getSelectedIndex())
    {
    case 0:fileformat=AudioFileFormat.Type.WAVE;break;
    case 1:fileformat=AudioFileFormat.Type.AU;break;
    case 2:fileformat=AudioFileFormat.Type.AIFF;break;
    case 3:fileformat=AudioFileFormat.Type.AIFC;break;
    case 4:fileformat=AudioFileFormat.Type.SND;break;
    }
AudInputLine tau=(AudInputLine)cmb_targetdatalines.getSelectedItem();
format=new AudioFormat(Float.parseFloat((String)cmb_sample.getSelectedItem()),Integer.parseInt((String)cmb_bits.getSelectedItem()),(cmb_monoORStereo.getSelectedIndex()+1)%2+1,!(Integer.parseInt((String)cmb_bits.getSelectedItem())==8),true);
AudInputLine taud=((AudInputLine)cmb_targetdatalines.getSelectedItem());
if(taud.lineInfo instanceof DataLine.Info&&((DataLine.Info)taud.lineInfo).isFormatSupported(format)==false)
    format=new AudioFormat(Float.parseFloat((String)cmb_sample.getSelectedItem()),Integer.parseInt((String)cmb_bits.getSelectedItem()),(cmb_monoORStereo.getSelectedIndex()+1)%2+1,!(Integer.parseInt((String)cmb_bits.getSelectedItem())==8),false);
try {
    inputline=AudioSystem.getTargetDataLine(format,tau.mixer.getMixerInfo());
    } catch (LineUnavailableException ex) {
        JOptionPane.showMessageDialog(this,ex.getMessage());
        }
buttonsEnable(false);
start=true;
    }//GEN-LAST:event_btn_startActionPerformed

    private void cmb_targetdatalinesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_targetdatalinesActionPerformed
if(cmb_targetdatalines.getItemCount()>0)
{
AudInputLine tau=(AudInputLine)cmb_targetdatalines.getSelectedItem();
jTextArea1.setText(tau.mixer.getMixerInfo().toString()+".\n"+tau.lineInfo.toString());
RefreshAudioFormats();
}
    }//GEN-LAST:event_cmb_targetdatalinesActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
cmb_targetdatalines.removeAllItems();
RefreshInputs();
    }//GEN-LAST:event_btn_refreshActionPerformed

public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {}
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_refresh;
    private javax.swing.JButton btn_start;
    private javax.swing.JButton btn_stop;
    private javax.swing.JComboBox cmb_bits;
    private javax.swing.JComboBox cmb_file_format;
    private javax.swing.JComboBox cmb_monoORStereo;
    private javax.swing.JComboBox cmb_sample;
    private javax.swing.JComboBox cmb_targetdatalines;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}

class AudInputLine
{
    public Mixer mixer;
    public Line.Info lineInfo;
    public String name;
    
    public String toString()
    {
        return name;
    }
}

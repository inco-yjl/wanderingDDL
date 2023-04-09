package desktop.application.wanderingddl;

import javax.sound.sampled.*;

public class ReadVoice {
    private static void debug(){
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixers) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            try {
                mixer.open();
                Line.Info[] lines = mixer.getTargetLineInfo();


                AudioFormat format = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        44100,
                        16, 2, 4,
                        44100, false);

                AudioFormat[] tdl = AudioSystem.getTargetFormats(AudioFormat.Encoding.PCM_SIGNED, format);

                for (Line.Info linfo : lines) {

                    //Line line = AudioSystem.getLine(linfo);


                    TargetDataLine line = null;
                    DataLine.Info info = new DataLine.Info(TargetDataLine.class,
                            format); // format is an AudioFormat object
                    if (!AudioSystem.isLineSupported(info))
                    {
                        System.out.println("line not supported:" + line );
                    }

                    try
                    {
                        line = (TargetDataLine) AudioSystem.getLine(info); //error
                        line.open(format);
                        System.out.println("line opened:" + line);

                        line.start();

                        byte[] buffer = new byte[1024];
                        int ii = 0;
                        int numBytesRead = 0;
                        while (ii++ < 100) {
                            // Read the next chunk of data from the TargetDataLine.
                            numBytesRead =  line.read(buffer, 0, buffer.length);

                            System.out.println("numBytesRead:" + numBytesRead);
                            if (numBytesRead == 0) continue;
                            // following is a quickie test to see if content is only 0 vals
                            // present in the data that was read.

                            for (int i = 0; i < 16; i++)
                            {
                                if (buffer[i] != 0)
                                    System.out.print(".");
                                else
                                    System.out.print("0");
                            }
                        }

                    } catch (LineUnavailableException ex) {
                        ex.printStackTrace();
                        //...
                    }
                }
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }
}

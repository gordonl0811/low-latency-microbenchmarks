package DisruptorPacketProcessorTests;

import PacketProcessor.DisruptorPacketProcessor.utils.PacketEvent;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import java.io.IOException;
import java.util.concurrent.ThreadFactory;
import org.junit.Test;

public class PacketReaderTest {

  @Test
  public void testProducerSendsPoisonPacket() throws IOException, InterruptedException {

    final String source = "src/test/resources/PacketProducerTest/input_single.pcap";

    ThreadFactory threadFactory = DaemonThreadFactory.INSTANCE;

    Disruptor<PacketEvent> producerDisruptor = new Disruptor<>(
        PacketEvent.EVENT_FACTORY,
        1024,
        threadFactory,
        ProducerType.SINGLE,
        new BusySpinWaitStrategy()
    );

//    PacketReader packetReader = new PacketReader(source, producerDisruptor);

    // TODO: Check for the single packet and the poison packet

  }

}

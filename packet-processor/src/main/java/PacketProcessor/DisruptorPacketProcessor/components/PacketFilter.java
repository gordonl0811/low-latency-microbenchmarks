package PacketProcessor.DisruptorPacketProcessor.components;

import PacketProcessor.DisruptorPacketProcessor.utils.PacketEvent;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import io.pkts.packet.Packet;
import io.pkts.protocol.Protocol;
import java.io.IOException;

public class PacketFilter implements PacketEventProducer, PacketEventConsumer {

  private final Disruptor<PacketEvent> tcpDisruptor;
  private final Disruptor<PacketEvent> udpDisruptor;

  private RingBuffer<PacketEvent> tcpRingBuffer;
  private RingBuffer<PacketEvent> udpRingBuffer;

  public PacketFilter(
      Disruptor<PacketEvent> tcpDisruptor,
      Disruptor<PacketEvent> udpDisruptor) {
    this.tcpDisruptor = tcpDisruptor;
    this.udpDisruptor = udpDisruptor;
  }

  @Override
  public void initialize() {
    tcpRingBuffer = tcpDisruptor.start();
    udpRingBuffer = udpDisruptor.start();
  }

  @Override
  public void onEvent(PacketEvent packetEvent, long l, boolean b) throws Exception {
    filterPacket(packetEvent.getValue());
  }

  private void filterPacket(Packet packet) throws IOException {
    if (packet.hasProtocol(Protocol.TCP)) {
      tcpRingBuffer.publishEvent((event, sequence, buffer) -> event.setValue(packet));
    } else if (packet.hasProtocol(Protocol.UDP)) {
      udpRingBuffer.publishEvent((event, sequence, buffer) -> event.setValue(packet));
    }
  }

}

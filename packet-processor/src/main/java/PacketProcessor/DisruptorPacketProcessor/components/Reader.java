package PacketProcessor.DisruptorPacketProcessor.components;

import PacketProcessor.DisruptorPacketProcessor.utils.PacketEvent;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import io.pkts.Pcap;

import java.io.IOException;
import java.util.List;

public class Reader implements PacketEventProducer {

    private final Pcap source;
    private final Disruptor<PacketEvent> readerDisruptor;
    private RingBuffer<PacketEvent> readerRingBuffer;


    public Reader(String source, Disruptor<PacketEvent> readerDisruptor)
            throws IOException {
        this.source = Pcap.openStream(source);
        this.readerDisruptor = readerDisruptor;
    }

    @Override
    public void initialize() {
        readerRingBuffer = this.readerDisruptor.start();
    }

    public void start() {

        try {
            // Load the packets into the RingBuffer
            this.source.loop(packet -> {
                readerRingBuffer.publishEvent((event, sequence, buffer) -> event.setValue(packet));
                return true;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

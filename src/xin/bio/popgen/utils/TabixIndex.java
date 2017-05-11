package xin.bio.popgen.utils;

import java.util.ArrayList;
import java.util.List;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import htsjdk.samtools.Chunk;
import htsjdk.samtools.util.BlockCompressedInputStream;
import htsjdk.samtools.util.CloserUtil;
import htsjdk.tribble.TribbleException;
import htsjdk.tribble.util.LittleEndianInputStream;

/**
 * Class {@code TabixIndex} is a customized TabixIndex class
 * in htsjdk for reading tabix index.
 * 
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 *
 */
public class TabixIndex {
	
	private static final byte[] MAGIC = {'T', 'B', 'I', 1};
    public static final int MAGIC_NUMBER;

    static {
        final ByteBuffer bb = ByteBuffer.allocate(MAGIC.length);
        bb.put(MAGIC);
        bb.flip();
        MAGIC_NUMBER = bb.order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
    
    private final List<Chunk> chunks;
    
    public TabixIndex(String fileName) {
    	chunks = new ArrayList<>();
    	LittleEndianInputStream dis = null;
        try {
            dis = new LittleEndianInputStream(
                    new BlockCompressedInputStream(new File(fileName)));
            if (dis.readInt() != MAGIC_NUMBER) {
                throw new TribbleException(String.format("Unexpected magic number 0x%x", MAGIC_NUMBER));
            }
            final int n_ref = dis.readInt();
            for (int i = 0; i < 6; i++) { dis.readInt(); }
            final int nameBlockSize = dis.readInt();
            final byte[] nameBlock = new byte[nameBlockSize];
            if (dis.read(nameBlock) != nameBlockSize) 
            	throw new EOFException("Premature end of file reading Tabix header");
            for (int i = 0; i < n_ref; i++) { loadSequence(dis); }
            CloserUtil.close(dis);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
        	CloserUtil.close(dis);
        }
    }
    
    private void loadSequence(final LittleEndianInputStream dis) throws IOException {
        final int n_bin = dis.readInt();
        if (n_bin == 0) return;
        for (int i = 0; i < n_bin; i++) { loadBin(dis); }
        loadLinearIndex(dis);
    }

    private void loadLinearIndex(final LittleEndianInputStream dis) throws IOException {
        final int n_intv = dis.readInt();
        for (int i = 0; i < n_intv; i++) { dis.readLong(); }
    }

    private void loadBin(final LittleEndianInputStream dis) throws IOException {
        dis.readInt();
        final int n_chunk = dis.readInt();
        for (int i = 0; i < n_chunk; i++) { chunks.add(loadChunk(dis)); }
    }

    private Chunk loadChunk(final LittleEndianInputStream dis) throws IOException {
        return new Chunk(dis.readLong(), dis.readLong());
    }

    public Chunk getChunk(int i) {
    	return chunks.get(i);
    }
    
}

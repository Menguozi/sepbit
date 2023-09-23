package gcsimulator.placement;

import gcsimulator.Log;
import gcsimulator.indexmap.IndexMap;
import gcsimulator.indexmap.IndexMapFactory;
import gcsimulator.Configs;

public class M2H extends Separator {
  public long writeCount;
  public IndexMap LWS;
  public IndexMap IRR;

  public M2H() {}

  @Override
  public void init(Log log, int numOpenSegments) {
    super.init(log, numOpenSegments);

    writeCount = 0;
    LWS = IndexMapFactory.getInstance(Configs.indexMapType, Configs.indexMapCache);
    LWS.setSize(Configs.volumeMaxLba.get(log.getId()) / 4096 + 1024);
    IRR = IndexMapFactory.getInstance(Configs.indexMapType, Configs.indexMapCache);
    IRR.setSize(Configs.volumeMaxLba.get(log.getId()) / 4096 + 1024);
  }

  @Override
  public void append(long lba) {
    writeCount++;
    if (LWS.containsKey(lba)) {
      IRR.put(lba, writeCount - LWS.get(lba));
      LWS.put(lba, writeCount);
    } else {
      LWS.put(lba, 0L);
    }
  }

  @Override
  public int classify(boolean isGcAppend, long lba) {
    if (isGcAppend) return 5;
    else {
      if(IRR.containsKey(lba))
      {
        long hotness = IRR.get(lba);
        System.out.println("lba: " + lba + "\thotness: " + hotness);
        return 1;
      }
      else return 0;
    }
  }
}

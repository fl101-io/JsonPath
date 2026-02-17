package com.jayway.jsonpath.internal.function;

import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JsonOrgJsonProvider;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.jayway.jsonpath.JsonPath.using;
import static org.assertj.core.api.Assertions.assertThat;

public class IndicatorPathFunctionTest extends BaseFunctionTest {
    private static final String prices = "{\"prices\": [109118.0, 109117.93, 109117.94, 109149.84, 109181.86, 109208.25, 109220.69, 109407.01, 109252.01, 109281.63, 109351.47, 109415.33, 109404.0, 109419.99, 109373.08, 109359.99, 109349.84, 109323.94, 109349.99, 109345.0, 109388.49, 109473.19, 109461.23, 109499.13, 109499.47, 109499.48, 109468.58, 109420.0, 109400.0, 109410.62, 109396.26, 109385.49, 109350.88, 109412.53, 109448.81, 109445.68, 109447.22, 109423.99, 109308.06, 109316.39, 109266.25, 109312.0, 109299.38, 109334.88, 109328.89, 109305.09, 109319.99, 109363.99, 109348.01, 109353.99, 109331.48, 109326.96, 109331.01, 109288.47, 109233.37, 109297.11, 109336.77, 109315.0, 109272.73, 109260.0, 109281.99, 109246.16, 109246.16, 109256.99, 109219.41, 109228.73, 109234.29, 109059.24, 109019.43, 109151.35, 109121.63, 109070.82, 109022.64, 109185.01, 109159.99, 109156.23, 109117.53, 109130.38, 109159.97, 109171.98, 109227.99, 109194.95, 109174.3, 109145.91, 109179.98, 109167.22, 109175.84, 109133.57, 109129.99, 109144.53, 109106.52, 109076.92, 109088.0, 109119.23, 109075.01, 109045.0, 109053.81, 109074.98, 109083.18, 109098.0]}";
    private static final String ema = "{\"ema12\": [109235.163333333, 109261.138205128, 109285.576942801, 109299.038951601, 109308.416035970, 109314.788953513, 109316.196806819, 109321.395759616, 109325.027181213, 109334.790691796, 109356.082893058, 109372.259371049, 109391.777929349, 109408.345940219, 109422.366564800, 109429.476324062, 109428.018428052, 109423.707900660, 109421.694377481, 109417.781396330, 109412.813489203, 109403.285260094, 109404.707527772, 109411.492523500, 109416.752135269, 109421.439499074, 109421.831883832, 109404.328517088, 109390.799514459, 109371.638050696, 109362.462965974, 109352.757894286, 109350.007449011, 109346.758610701, 109340.348055209, 109337.216046715, 109341.335116451, 109342.362021613, 109344.150941365, 109342.201565770, 109339.856709498, 109338.495677267, 109330.799419226, 109315.810277807, 109312.933311990, 109316.600494761, 109316.354264798, 109309.642839444, 109302.005479530, 109298.926174987, 109290.808301912, 109283.939332387, 109279.793281251, 109270.503545674, 109264.076846339, 109259.494254595, 109228.685907734, 109196.492691159, 109189.547661750, 109179.098790712, 109162.440515218, 109140.932743646, 109147.713860008, 109149.602496930, 109150.622112787, 109145.531018512, 109143.200092587, 109145.780078343, 109149.810835521, 109161.838399287, 109166.932491704, 109168.065954519, 109164.657346131, 109167.014677496, 109167.046265573, 109168.399147793, 109163.040817363, 109157.956076230, 109155.890526041, 109148.295060496, 109137.314281958, 109129.727469349, 109128.112474065, 109119.942862670, 109108.413191490, 109100.012700492, 109096.161515801, 109094.164359524, 109094.754458059], " +
                                        "\"ema26\": [109329.56846154, 109339.86561254, 109345.80149309, 109349.81619730, 109354.32018269, 109357.42683582, 109359.50558873, 109358.86665623, 109362.84171873, 109369.20973956, 109374.87420330, 109380.23315120, 109383.47439926, 109377.88814747, 109373.33272913, 109365.40067512, 109361.44506956, 109356.84765700, 109355.22042315, 109353.27002143, 109349.70113096, 109347.50030644, 109348.72176522, 109348.66904187, 109349.06318692, 109347.76072863, 109346.21993392, 109345.09327215, 109340.89895569, 109332.93384786, 109330.28022950, 109330.76095324, 109329.59347522, 109325.38136595, 109320.53830180, 109317.68287204, 109312.38488152, 109307.47933474, 109303.73938402, 109297.49276298, 109292.39922498, 109288.09483795, 109271.14262773, 109252.49724790, 109245.00485916, 109235.86598071, 109223.64035251, 109208.75143751, 109206.99281251, 109203.51112269, 109200.00881731, 109193.89927528, 109189.19414378, 109187.02939239, 109185.91462258, 109189.03131721, 109189.46973815, 109188.34605385, 109185.20264245, 109184.81578005, 109183.51238893, 109182.94406383, 109179.28672577, 109175.63511645, 109173.33103375, 109168.38206829, 109161.60710027, 109156.15472247, 109153.41955784, 109147.61144245, 109140.01059486, 109133.62536561, 109129.28126445, 109125.86635597, 109123.80218146]}";
    private static final String empty = "{\"prices\":[]}";
    private static final List<Double> expectedSMA26;

    static {
        expectedSMA26 = new ArrayList<>();
        expectedSMA26.add(109329.5685d);
        expectedSMA26.add(109343.0523d);
        expectedSMA26.add(109354.6704d);
        expectedSMA26.add(109365.5188d);
        expectedSMA26.add(109375.5488d);
        expectedSMA26.add(109383.7950d);
        expectedSMA26.add(109390.6119d);
        expectedSMA26.add(109395.6192d);
        expectedSMA26.add(109395.8315d);
        expectedSMA26.add(109403.4008d);
        expectedSMA26.add(109409.7104d);
        expectedSMA26.add(109413.3931d);
        expectedSMA26.add(109413.7262d);
        expectedSMA26.add(109410.0362d);
        expectedSMA26.add(109406.0515d);
        expectedSMA26.add(109401.9427d);
        expectedSMA26.add(109400.0969d);
        expectedSMA26.add(109398.1562d);
        expectedSMA26.add(109398.5769d);
        expectedSMA26.add(109397.7654d);
        expectedSMA26.add(109396.2304d);
        expectedSMA26.add(109393.5958d);
        expectedSMA26.add(109389.3958d);
        expectedSMA26.add(109385.0412d);
        expectedSMA26.add(109379.4588d);
        expectedSMA26.add(109372.9977d);
        expectedSMA26.add(109366.3623d);
        expectedSMA26.add(109361.0712d);
        expectedSMA26.add(109356.0123d);
        expectedSMA26.add(109349.6035d);
        expectedSMA26.add(109345.2377d);
        expectedSMA26.add(109342.9496d);
        expectedSMA26.add(109340.2385d);
        expectedSMA26.add(109337.2327d);
        expectedSMA26.add(109331.3662d);
        expectedSMA26.add(109324.9500d);
        expectedSMA26.add(109317.2762d);
        expectedSMA26.add(109309.5431d);
        expectedSMA26.add(109303.1200d);
        expectedSMA26.add(109299.7104d);
        expectedSMA26.add(109296.3388d);
        expectedSMA26.add(109295.1096d);
        expectedSMA26.add(109285.3881d);
        expectedSMA26.add(109274.6208d);
        expectedSMA26.add(109267.5619d);
        expectedSMA26.add(109259.5904d);
        expectedSMA26.add(109250.5800d);
        expectedSMA26.add(109239.1435d);
        expectedSMA26.add(109232.2596d);
        expectedSMA26.add(109225.0281d);
        expectedSMA26.add(109217.4219d);
        expectedSMA26.add(109209.1931d);
        expectedSMA26.add(109201.6323d);
        expectedSMA26.add(109195.0538d);
        expectedSMA26.add(109190.5735d);
        expectedSMA26.add(109190.3665d);
        expectedSMA26.add(109186.4373d);
        expectedSMA26.add(109180.1885d);
        expectedSMA26.add(109173.6850d);
        expectedSMA26.add(109170.1177d);
        expectedSMA26.add(109166.5492d);
        expectedSMA26.add(109162.4665d);
        expectedSMA26.add(109158.1362d);
        expectedSMA26.add(109153.6681d);
        expectedSMA26.add(109149.3427d);
        expectedSMA26.add(109145.0008d);
        expectedSMA26.add(109139.1619d);
        expectedSMA26.add(109133.5354d);
        expectedSMA26.add(109135.8427d);
        expectedSMA26.add(109137.9804d);
        expectedSMA26.add(109133.8900d);
        expectedSMA26.add(109131.2815d);
        expectedSMA26.add(109131.4415d);
        expectedSMA26.add(109133.7700d);
        expectedSMA26.add(109130.4235d);
    }

    private static final List<Double> expectedEMA26;

    static {
        expectedEMA26 = new ArrayList<>();
        expectedEMA26.add(109329.5685d);
        expectedEMA26.add(109339.8656d);
        expectedEMA26.add(109345.8015d);
        expectedEMA26.add(109349.8162d);
        expectedEMA26.add(109354.3202d);
        expectedEMA26.add(109357.4268d);
        expectedEMA26.add(109359.5056d);
        expectedEMA26.add(109358.8667d);
        expectedEMA26.add(109362.8417d);
        expectedEMA26.add(109369.2097d);
        expectedEMA26.add(109374.8742d);
        expectedEMA26.add(109380.2332d);
        expectedEMA26.add(109383.4744d);
        expectedEMA26.add(109377.8881d);
        expectedEMA26.add(109373.3327d);
        expectedEMA26.add(109365.4007d);
        expectedEMA26.add(109361.4451d);
        expectedEMA26.add(109356.8477d);
        expectedEMA26.add(109355.2204d);
        expectedEMA26.add(109353.2700d);
        expectedEMA26.add(109349.7011d);
        expectedEMA26.add(109347.5003d);
        expectedEMA26.add(109348.7218d);
        expectedEMA26.add(109348.6690d);
        expectedEMA26.add(109349.0632d);
        expectedEMA26.add(109347.7607d);
        expectedEMA26.add(109346.2199d);
        expectedEMA26.add(109345.0933d);
        expectedEMA26.add(109340.8990d);
        expectedEMA26.add(109332.9338d);
        expectedEMA26.add(109330.2802d);
        expectedEMA26.add(109330.7610d);
        expectedEMA26.add(109329.5935d);
        expectedEMA26.add(109325.3814d);
        expectedEMA26.add(109320.5383d);
        expectedEMA26.add(109317.6829d);
        expectedEMA26.add(109312.3849d);
        expectedEMA26.add(109307.4793d);
        expectedEMA26.add(109303.7394d);
        expectedEMA26.add(109297.4928d);
        expectedEMA26.add(109292.3992d);
        expectedEMA26.add(109288.0948d);
        expectedEMA26.add(109271.1426d);
        expectedEMA26.add(109252.4972d);
        expectedEMA26.add(109245.0049d);
        expectedEMA26.add(109235.8660d);
        expectedEMA26.add(109223.6404d);
        expectedEMA26.add(109208.7514d);
        expectedEMA26.add(109206.9928d);
        expectedEMA26.add(109203.5111d);
        expectedEMA26.add(109200.0088d);
        expectedEMA26.add(109193.8993d);
        expectedEMA26.add(109189.1941d);
        expectedEMA26.add(109187.0294d);
        expectedEMA26.add(109185.9146d);
        expectedEMA26.add(109189.0313d);
        expectedEMA26.add(109189.4697d);
        expectedEMA26.add(109188.3461d);
        expectedEMA26.add(109185.2026d);
        expectedEMA26.add(109184.8158d);
        expectedEMA26.add(109183.5124d);
        expectedEMA26.add(109182.9441d);
        expectedEMA26.add(109179.2867d);
        expectedEMA26.add(109175.6351d);
        expectedEMA26.add(109173.3310d);
        expectedEMA26.add(109168.3821d);
        expectedEMA26.add(109161.6071d);
        expectedEMA26.add(109156.1547d);
        expectedEMA26.add(109153.4196d);
        expectedEMA26.add(109147.6114d);
        expectedEMA26.add(109140.0106d);
        expectedEMA26.add(109133.6254d);
        expectedEMA26.add(109129.2813d);
        expectedEMA26.add(109125.8664d);
        expectedEMA26.add(109123.8022d);
    }

    private static final List<Double> expectedRSI14;

    static {
        expectedRSI14 = new ArrayList<>();
        expectedRSI14.add(68.7091d);
        expectedRSI14.add(67.3171d);
        expectedRSI14.add(66.1971d);
        expectedRSI14.add(63.3030d);
        expectedRSI14.add(64.9623d);
        expectedRSI14.add(64.3619d);
        expectedRSI14.add(67.2063d);
        expectedRSI14.add(71.9086d);
        expectedRSI14.add(70.3741d);
        expectedRSI14.add(72.3852d);
        expectedRSI14.add(72.4033d);
        expectedRSI14.add(72.4039d);
        expectedRSI14.add(67.7256d);
        expectedRSI14.add(61.0472d);
        expectedRSI14.add(58.4900d);
        expectedRSI14.add(59.4610d);
        expectedRSI14.add(57.5022d);
        expectedRSI14.add(56.0119d);
        expectedRSI14.add(51.4014d);
        expectedRSI14.add(58.0287d);
        expectedRSI14.add(61.3674d);
        expectedRSI14.add(60.9172d);
        expectedRSI14.add(61.0685d);
        expectedRSI14.add(57.4544d);
        expectedRSI14.add(43.5898d);
        expectedRSI14.add(44.6239d);
        expectedRSI14.add(39.8846d);
        expectedRSI14.add(45.5654d);
        expectedRSI14.add(44.3212d);
        expectedRSI14.add(48.5751d);
        expectedRSI14.add(47.9099d);
        expectedRSI14.add(45.2583d);
        expectedRSI14.add(47.2275d);
        expectedRSI14.add(52.6449d);
        expectedRSI14.add(50.6128d);
        expectedRSI14.add(51.3693d);
        expectedRSI14.add(48.3660d);
        expectedRSI14.add(47.7621d);
        expectedRSI14.add(48.3840d);
        expectedRSI14.add(42.6420d);
        expectedRSI14.add(36.5858d);
        expectedRSI14.add(46.1192d);
        expectedRSI14.add(51.0502d);
        expectedRSI14.add(48.4301d);
        expectedRSI14.add(43.7365d);
        expectedRSI14.add(42.4036d);
        expectedRSI14.add(45.4937d);
        expectedRSI14.add(41.5793d);
        expectedRSI14.add(41.5793d);
        expectedRSI14.add(43.2898d);
        expectedRSI14.add(39.0205d);
        expectedRSI14.add(40.5855d);
        expectedRSI14.add(41.5492d);
        expectedRSI14.add(26.8065d);
        expectedRSI14.add(24.6633d);
        expectedRSI14.add(41.3871d);
        expectedRSI14.add(39.2719d);
        expectedRSI14.add(35.8945d);
        expectedRSI14.add(32.9967d);
        expectedRSI14.add(48.1800d);
        expectedRSI14.add(46.4339d);
        expectedRSI14.add(46.1631d);
        expectedRSI14.add(43.3606d);
        expectedRSI14.add(44.5640d);
        expectedRSI14.add(47.3388d);
        expectedRSI14.add(48.4663d);
        expectedRSI14.add(53.4696d);
        expectedRSI14.add(50.3633d);
        expectedRSI14.add(48.4681d);
        expectedRSI14.add(45.9102d);
        expectedRSI14.add(49.3639d);
        expectedRSI14.add(48.1245d);
        expectedRSI14.add(49.0550d);
        expectedRSI14.add(44.8103d);
        expectedRSI14.add(44.4594d);
        expectedRSI14.add(46.2987d);
        expectedRSI14.add(42.3504d);
        expectedRSI14.add(39.5238d);
        expectedRSI14.add(41.1083d);
        expectedRSI14.add(45.4469d);
        expectedRSI14.add(40.8571d);
        expectedRSI14.add(38.0487d);
        expectedRSI14.add(39.3663d);
        expectedRSI14.add(42.5295d);
        expectedRSI14.add(43.7536d);
        expectedRSI14.add(45.9924d);
    }

    private static final List<Double> expectedStochRSI14;

    static {
        expectedStochRSI14 = new ArrayList<>();
        expectedStochRSI14.add(0.0000d);
        expectedStochRSI14.add(0.0000d);
        expectedStochRSI14.add(6.9791d);
        expectedStochRSI14.add(0.0000d);
        expectedStochRSI14.add(0.0000d);
        expectedStochRSI14.add(0.0000d);
        expectedStochRSI14.add(31.5548d);
        expectedStochRSI14.add(47.4518d);
        expectedStochRSI14.add(45.3081d);
        expectedStochRSI14.add(46.0287d);
        expectedStochRSI14.add(28.8204d);
        expectedStochRSI14.add(0.0000d);
        expectedStochRSI14.add(4.2843d);
        expectedStochRSI14.add(0.0000d);
        expectedStochRSI14.add(26.4435d);
        expectedStochRSI14.add(20.6519d);
        expectedStochRSI14.add(40.4530d);
        expectedStochRSI14.add(37.3570d);
        expectedStochRSI14.add(25.0138d);
        expectedStochRSI14.add(34.1803d);
        expectedStochRSI14.add(59.3979d);
        expectedStochRSI14.add(50.6430d);
        expectedStochRSI14.add(54.2142d);
        expectedStochRSI14.add(48.2725d);
        expectedStochRSI14.add(61.7344d);
        expectedStochRSI14.add(66.6076d);
        expectedStochRSI14.add(21.6095d);
        expectedStochRSI14.add(0.0000d);
        expectedStochRSI14.add(59.3642d);
        expectedStochRSI14.add(90.0695d);
        expectedStochRSI14.add(73.7546d);
        expectedStochRSI14.add(44.5271d);
        expectedStochRSI14.add(36.2275d);
        expectedStochRSI14.add(55.4692d);
        expectedStochRSI14.add(33.7780d);
        expectedStochRSI14.add(33.7780d);
        expectedStochRSI14.add(46.3487d);
        expectedStochRSI14.add(16.8328d);
        expectedStochRSI14.add(27.6523d);
        expectedStochRSI14.add(34.3151d);
        expectedStochRSI14.add(0.0000d);
        expectedStochRSI14.add(0.0000d);
        expectedStochRSI14.add(63.3791d);
        expectedStochRSI14.add(61.4666d);
        expectedStochRSI14.add(53.9178d);
        expectedStochRSI14.add(40.0062d);
        expectedStochRSI14.add(100.0000d);
        expectedStochRSI14.add(92.5750d);
        expectedStochRSI14.add(91.4237d);
        expectedStochRSI14.add(79.5065d);
        expectedStochRSI14.add(84.6239d);
        expectedStochRSI14.add(96.4229d);
        expectedStochRSI14.add(100.0000d);
        expectedStochRSI14.add(100.0000d);
        expectedStochRSI14.add(89.2166d);
        expectedStochRSI14.add(75.5702d);
        expectedStochRSI14.add(63.0762d);
        expectedStochRSI14.add(79.9455d);
        expectedStochRSI14.add(73.8919d);
        expectedStochRSI14.add(56.3304d);
        expectedStochRSI14.add(14.3409d);
        expectedStochRSI14.add(10.8698d);
        expectedStochRSI14.add(29.0639d);
        expectedStochRSI14.add(0.0000d);
        expectedStochRSI14.add(0.0000d);
        expectedStochRSI14.add(11.3621d);
        expectedStochRSI14.add(42.4727d);
        expectedStochRSI14.add(12.3006d);
        expectedStochRSI14.add(0.0000d);
        expectedStochRSI14.add(11.6451d);
        expectedStochRSI14.add(39.6006d);
        expectedStochRSI14.add(51.8327d);
        expectedStochRSI14.add(72.1737d);
    }

    private static final List<Double> expectedMACD1226;

    static {
        expectedMACD1226 = new ArrayList<>();
        expectedMACD1226.add(92.7981d);
        expectedMACD1226.add(89.6107d);
        expectedMACD1226.add(82.2169d);
        expectedMACD1226.add(73.8917d);
        expectedMACD1226.add(67.3742d);
        expectedMACD1226.add(60.3546d);
        expectedMACD1226.add(53.3079d);
        expectedMACD1226.add(44.4186d);
        expectedMACD1226.add(41.8658d);
        expectedMACD1226.add(42.2828d);
        expectedMACD1226.add(41.8779d);
        expectedMACD1226.add(41.2063d);
        expectedMACD1226.add(38.3575d);
        expectedMACD1226.add(26.4404d);
        expectedMACD1226.add(17.4668d);
        expectedMACD1226.add(6.2374d);
        expectedMACD1226.add(1.0179d);
        expectedMACD1226.add(-4.0898d);
        expectedMACD1226.add(-5.2130d);
        expectedMACD1226.add(-6.5114d);
        expectedMACD1226.add(-9.3531d);
        expectedMACD1226.add(-10.2843d);
        expectedMACD1226.add(-7.3866d);
        expectedMACD1226.add(-6.3070d);
        expectedMACD1226.add(-4.9122d);
        expectedMACD1226.add(-5.5592d);
        expectedMACD1226.add(-6.3632d);
        expectedMACD1226.add(-6.5976d);
        expectedMACD1226.add(-10.0995d);
        expectedMACD1226.add(-17.1236d);
        expectedMACD1226.add(-17.3469d);
        expectedMACD1226.add(-14.1605d);
        expectedMACD1226.add(-13.2392d);
        expectedMACD1226.add(-15.7385d);
        expectedMACD1226.add(-18.5328d);
        expectedMACD1226.add(-18.7567d);
        expectedMACD1226.add(-21.5766d);
        expectedMACD1226.add(-23.5400d);
        expectedMACD1226.add(-23.9461d);
        expectedMACD1226.add(-26.9892d);
        expectedMACD1226.add(-28.3224d);
        expectedMACD1226.add(-28.6006d);
        expectedMACD1226.add(-42.4567d);
        expectedMACD1226.add(-56.0046d);
        expectedMACD1226.add(-55.4572d);
        expectedMACD1226.add(-56.7672d);
        expectedMACD1226.add(-61.1998d);
        expectedMACD1226.add(-67.8187d);
        expectedMACD1226.add(-59.2790d);
        expectedMACD1226.add(-53.9086d);
        expectedMACD1226.add(-49.3867d);
        expectedMACD1226.add(-48.3683d);
        expectedMACD1226.add(-45.9941d);
        expectedMACD1226.add(-41.2493d);
        expectedMACD1226.add(-36.1038d);
        expectedMACD1226.add(-27.1929d);
        expectedMACD1226.add(-22.5372d);
        expectedMACD1226.add(-20.2801d);
        expectedMACD1226.add(-20.5453d);
        expectedMACD1226.add(-17.8011d);
        expectedMACD1226.add(-16.4661d);
        expectedMACD1226.add(-14.5449d);
        expectedMACD1226.add(-16.2459d);
        expectedMACD1226.add(-17.6790d);
        expectedMACD1226.add(-17.4405d);
        expectedMACD1226.add(-20.0870d);
        expectedMACD1226.add(-24.2928d);
        expectedMACD1226.add(-26.4273d);
        expectedMACD1226.add(-25.3071d);
        expectedMACD1226.add(-27.6686d);
        expectedMACD1226.add(-31.5974d);
        expectedMACD1226.add(-33.6127d);
        expectedMACD1226.add(-33.1197d);
        expectedMACD1226.add(-31.7020d);
        expectedMACD1226.add(-29.0477d);
    }

    public static final Configuration JSON_PATH_CONF = Configuration.defaultConfiguration()
            .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    public static Iterable<Configuration> configurations() {
        return Configurations.configurations();
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testSMA(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(prices).read("$.prices.sma(26,4)", ArrayList.class);
            assertThat(result).isEqualTo(expectedSMA26);
        } else {
            JSONArray result = using(conf).parse(prices).read("$.prices.sma(26,4)", JSONArray.class);
            assertThat(toList(result)).isEqualTo(expectedSMA26);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testSMAOfEmpty(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(empty).read("$.prices.sma(26,4)", ArrayList.class);
            assertThat(result).isEmpty();
        } else {
            JSONArray result = using(conf).parse(empty).read("$.prices.sma(26,4)", JSONArray.class);
            assertThat(toList(result)).isEmpty();
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testSMAIncorrectWindow(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(prices).read("$.prices.sma(101,4)", ArrayList.class);
            assertThat(result).isEmpty();
        } else {
            JSONArray result = using(conf).parse(prices).read("$.prices.sma(101,4)", JSONArray.class);
            assertThat(toList(result)).isEmpty();
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testSMASingleResult(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(prices).read("$.prices.sma(100,4)", ArrayList.class);
            assertThat(result).isEqualTo(Collections.singletonList(109261.6352d));
        } else {
            JSONArray result = using(conf).parse(prices).read("$.prices.sma(100,4)", JSONArray.class);
            assertThat(toList(result)).isEqualTo(Collections.singletonList(109261.6352d));
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testEMA(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(prices).read("$.prices.ema(26,4)", ArrayList.class);
            assertThat(result).isEqualTo(expectedEMA26);
        }  else {
            JSONArray result = using(conf).parse(prices).read("$.prices.ema(26,4)", JSONArray.class);
            assertThat(toList(result)).isEqualTo(expectedEMA26);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testEMAOfEmpty(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(empty).read("$.prices.ema(26,4)", ArrayList.class);
            assertThat(result).isEmpty();
        } else {
            JSONArray result = using(conf).parse(empty).read("$.prices.ema(26,4)", JSONArray.class);
            assertThat(toList(result)).isEmpty();
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testEMAIncorrectWindow(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(prices).read("$.prices.ema(101,4)", ArrayList.class);
            assertThat(result).isEmpty();
        } else {
            JSONArray result = using(conf).parse(prices).read("$.prices.ema(101,4)", JSONArray.class);
            assertThat(toList(result)).isEmpty();
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testEMASingleResult(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(prices).read("$.prices.ema(100,4)", ArrayList.class);
            assertThat(result).isEqualTo(Collections.singletonList(109261.6352d));
        } else {
            JSONArray result = using(conf).parse(prices).read("$.prices.ema(100,4)", JSONArray.class);
            assertThat(toList(result)).isEqualTo(Collections.singletonList(109261.6352d));
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testRSI(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(prices).read("$.prices.rsi(14,4)", ArrayList.class);
            assertThat(result).isEqualTo(expectedRSI14);
        } else {
            JSONArray result = using(conf).parse(prices).read("$.prices.rsi(14,4)", JSONArray.class);
            assertThat(toList(result)).isEqualTo(expectedRSI14);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testRSIOfEmpty(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(empty).read("$.prices.rsi(14,4)", ArrayList.class);
            assertThat(result).isEmpty();
        } else {
            JSONArray result = using(conf).parse(empty).read("$.prices.rsi(14,4)", JSONArray.class);
            assertThat(toList(result)).isEmpty();
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testRSIIncorrectWindow(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(prices).read("$.prices.rsi(100,4)", ArrayList.class);
            assertThat(result).isEmpty();
        } else {
            JSONArray result = using(conf).parse(prices).read("$.prices.rsi(100,4)", JSONArray.class);
            assertThat(toList(result)).isEmpty();
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testStochRSI(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(prices).read("$.prices.stochrsi(14,4)", ArrayList.class);
            assertThat(result).isEqualTo(expectedStochRSI14);
        } else {
            JSONArray result = using(conf).parse(prices).read("$.prices.stochrsi(14,4)", JSONArray.class);
            assertThat(toList(result)).isEqualTo(expectedStochRSI14);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testStochRSIOfEmpty(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(empty).read("$.prices.stochrsi(14,4)", ArrayList.class);
            assertThat(result).isEmpty();
        } else {
            JSONArray result = using(conf).parse(empty).read("$.prices.stochrsi(14,4)", JSONArray.class);
            assertThat(toList(result)).isEmpty();
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testStochRSIIncorrectWindow(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(prices).read("$.prices.stochrsi(51,4)", ArrayList.class);
            assertThat(result).isEmpty();
        } else {
            JSONArray result = using(conf).parse(prices).read("$.prices.stochrsi(51,4)", JSONArray.class);
            assertThat(toList(result)).isEmpty();
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testDIFF(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(ema).read("$.ema12.diff($.ema26,4)", ArrayList.class);
            assertThat(result).isEqualTo(expectedMACD1226);
        }  else {
            JSONArray result = using(conf).parse(ema).read("$.ema12.diff($.ema26,4)", JSONArray.class);
            assertThat(toList(result)).isEqualTo(expectedMACD1226);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testDIFFOfEmpty(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(empty).read("$.prices.diff($.prices,4)", ArrayList.class);
            assertThat(result).isEmpty();
        } else {
            JSONArray result = using(conf).parse(empty).read("$.prices.diff($.prices,4)", JSONArray.class);
            assertThat(toList(result)).isEmpty();
        }
    }

    @Test
    public void testKlines() throws Exception {
        String klines = IOUtils.toString(getClass().getResourceAsStream("/klines.json"));
        DocumentContext doc = JsonPath.using(JSON_PATH_CONF).parse(klines);
        Object o = doc.read("$.klines[*].closePrice");
        Object prices = "JSONArray".equals(o.getClass().getSimpleName()) ? new ArrayList<>(((net.minidev.json.JSONArray) o)) : o;
        doc.set("$.prices", prices);
        Object result = doc.read("$.prices.rsi(6,4)");
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOfAny(ArrayList.class);
        assertThat(((ArrayList<?>)result).get(43)).isEqualTo(26.1033d);
    }

    private static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            list.add(array.get(i));           // works with String, Integer, Boolean, JSONObject, ...
        }
        return list;
    }
}

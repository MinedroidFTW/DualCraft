package dualcraft.org.server.classic.model.impl.builders;

/*License
====================
Copyright (c) 2010-2012 Daniel Vidmar

We use a modified GNU gpl v 3 license for this.

GNU gpl v 3 is included in License.txt

The modified part of the license is some additions which state the following:

"Redistributions of this project in source or binary must give credit to UnXoft Interactive and DualCraft"
"Redistributions of this project in source or binary must modify at least 300 lines of code in order to release
an initial version. This will require documentation or proof of the 300 modified lines of code."
"Our developers reserve the right to add any additions made to a redistribution of DualCraft into the main
project"
"Our developers reserver the right if they suspect a closed source software using any code from our project
to request to overview the source code of the suspected software. If the owner of the suspected software refuses 
to allow a devloper to overview the code then we shall/are granted the right to persue legal action against
him/her"*/

import java.awt.Point;
import java.util.LinkedList;
import java.util.ArrayList;
import java.lang.Math;

import dualcraft.org.server.classic.model.Builder;
import dualcraft.org.server.classic.model.BlockConstants;
import dualcraft.org.server.classic.model.Position;
import dualcraft.org.server.classic.model.Level;
import org.slf4j.*;

/**
 * Builds a level.
 * 
 */

public class LandscapeBuilder extends Builder {


	public LandscapeBuilder(Level level) {
		super(level);
	}

	private static final Logger logger = LoggerFactory.getLogger(LandscapeBuilder.class);

	public void generate() {
		m_seed = m_random.nextInt();
		generateTerrain();
		generateCaverns((m_width+m_height)/2);
		buildLavaBed(2);
		simulateOceanFlood();
		if (m_theme.equalsIgnoreCase("winter")) {
			makeSnow();
		} else if (m_theme.equalsIgnoreCase("oasis")) {
			makeSand();
		}
		plantTrees();
	}

	public String getTheme() {
		return m_theme;
	}

	public void generateTerrain() {
		for(int x = 0;x<m_width;x++) {
			for(int y = 0;y<m_height;y++) {
				double value;
				value = getValue(x/200.0, y/200.0, m_depth/2/200.0);
				int height = (int)(Math.max(-1,Math.min(1, value))*m_depth/4);
				for(int z = 0;z<m_depth/2+height-5;z++) {
					m_blocks[x][y][z] = BlockConstants.ROCK;
				}
				for(int z = m_depth/2+height-5;z<m_depth/2+height;z++) {
					m_blocks[x][y][z] = BlockConstants.DIRT;
				}
				m_blocks[x][y][m_depth/2+height] = BlockConstants.GRASS;
				for(int z = m_depth/2+height+1;z<m_depth;z++) {
					m_blocks[x][y][z] = BlockConstants.AIR;
				}
			}
		}
	}

	private double m_frequency = 3.0;
	private double m_lacunarity = 2.0;
	private int m_noiseQuality = 0;
	private int m_octaveCount = 6;
	private double m_persistence = 0.5;
	private int m_seed = 0;
	

	private double getValue(double x, double y, double z) {
		double value = 0.0;
		double signal = 0.0;
		double curPersistence = 1.0;
		double nx, ny, nz;
		int seed;

		x *= m_frequency;
		y *= m_frequency;
		z *= m_frequency;

		for(int curOctave = 0;curOctave < m_octaveCount;curOctave++) {
			nx = MakeInt32Range(x);
			ny = MakeInt32Range(y);
			nz = MakeInt32Range(z);

			seed = (m_seed+curOctave) & 0xffffffff;

			signal = GradientCoherentNoise3D(nx, ny, nz, seed, m_noiseQuality);

			value += signal * curPersistence;

			x *= m_lacunarity;
			y *= m_lacunarity;
			z *= m_lacunarity;
			curPersistence *= m_persistence;

		}

		return value;
	}

	private double MakeInt32Range(double n) {
		return n;
		/*if (n >= 1073741824.0) {
			return (2.0 * (n % 1073741824.0)) - 1073741824.0;
		} else if (n <= -1073741824.0) {
			return (2.0 * (n % 1073741824.0)) + 1073741824.0;
		} else {
			return n;
		}*/
	}

	private double GradientCoherentNoise3D(double x, double y, double z, int seed, int quality) {
		// Create a unit-length cube aligned along an integer boundary.  This cube
		// surrounds the input point.
		int x0 = (x > 0.0? (int)x: (int)x - 1);
		int x1 = x0 + 1;
		//int y0 = (y > 0.0? (int)y: (int)y - 1);
		int y0 = (int)y;
		int y1 = y0 + 1;
		int z0 = (z > 0.0? (int)z: (int)z - 1);
		int z1 = z0 + 1;

		// Map the difference between the coordinates of the input value and the
		// coordinates of the cube's outer-lower-left vertex onto an S-curve.
		double xs = 0, ys = 0, zs = 0;
		switch (quality) {
			case 0:
				xs = (x - (double)x0);
				ys = (y - (double)y0);
				zs = (z - (double)z0);
				break;
			case 1:
				xs = SCurve3 (x - (double)x0);
				ys = SCurve3 (y - (double)y0);
				zs = SCurve3 (z - (double)z0);
				break;
			case 2:
				xs = SCurve5 (x - (double)x0);
				ys = SCurve5 (y - (double)y0);
				zs = SCurve5 (z - (double)z0);
				break;
		}

		// Now calculate the noise values at each vertex of the cube.  To generate
		// the coherent-noise value at the input point, interpolate these eight
		// noise values using the S-curve value as the interpolant (trilinear
		// interpolation.)
		double n0, n1, ix0, ix1, iy0, iy1;
		n0   = GradientNoise3D (x, y, z, x0, y0, z0, seed);
		n1   = GradientNoise3D (x, y, z, x1, y0, z0, seed);
		ix0  = LinearInterp (n0, n1, xs);
		n0   = GradientNoise3D (x, y, z, x0, y1, z0, seed);
		n1   = GradientNoise3D (x, y, z, x1, y1, z0, seed);
		ix1  = LinearInterp (n0, n1, xs);
		iy0  = LinearInterp (ix0, ix1, ys);
		n0   = GradientNoise3D (x, y, z, x0, y0, z1, seed);
		n1   = GradientNoise3D (x, y, z, x1, y0, z1, seed);
		ix0  = LinearInterp (n0, n1, xs);
		n0   = GradientNoise3D (x, y, z, x0, y1, z1, seed);
		n1   = GradientNoise3D (x, y, z, x1, y1, z1, seed);
		ix1  = LinearInterp (n0, n1, xs);
		iy1  = LinearInterp (ix0, ix1, ys);

		return LinearInterp (iy0, iy1, zs);
		//return (iy0+ix0)/2;
	}

	public double GradientNoise3D (double fx, double fy, double fz, int ix, int iy, int iz, int seed) {
		// Randomly generate a gradient vector given the integer coordinates of the
		// input value.  This implementation generates a random number and uses it
		// as an index into a normalized-vector lookup table.
		int vectorIndex = (
		  X_NOISE_GEN    * ix
		+ Y_NOISE_GEN    * iy
		+ Z_NOISE_GEN    * iz
		+ SEED_NOISE_GEN * seed)
		& 0xffffffff;
		vectorIndex ^= (vectorIndex >> SHIFT_NOISE_GEN);
		vectorIndex &= 0xff;

		double xvGradient = g_randomVectors[(vectorIndex << 2)    ];
		double yvGradient = g_randomVectors[(vectorIndex << 2) + 1];
		double zvGradient = g_randomVectors[(vectorIndex << 2) + 2];

		// Set up us another vector equal to the distance between the two vectors
		// passed to this function.
		double xvPoint = (fx - (double)ix);
		double yvPoint = (fy - (double)iy);
		double zvPoint = (fz - (double)iz);

		// Now compute the dot product of the gradient vector with the distance
		// vector.  The resulting value is gradient noise.  Apply a scaling value
		// so that this noise value ranges from -1.0 to 1.0.
		return ((xvGradient * xvPoint)
		+ (yvGradient * yvPoint)
		+ (zvGradient * zvPoint)) * 2.12;
	}

	private double LinearInterp(double n0, double n1, double a) {
		return ((1.0 - a) * n0) + (a * n1);
	}

	private double SCurve3(double a) {
		return (a * a * (3.0 - 2.0 * a));
	}

	private double SCurve5(double a) {
		double a3 = a * a * a;
		double a4 = a3 * a;
		double a5 = a4 * a;
		return (6.0 * a5) - (15.0 * a4) + (10.0 * a3);
	}

	private static final int X_NOISE_GEN = 1619;
	private static final int Y_NOISE_GEN = 31337;
	private static final int Z_NOISE_GEN = 6971;
	private static final int SEED_NOISE_GEN = 1013;
	private static final int SHIFT_NOISE_GEN = 8;

	private static final double[] g_randomVectors = {
    -0.763874, -0.596439, -0.246489, 0.0,
    0.396055, 0.904518, -0.158073, 0.0,
    -0.499004, -0.8665, -0.0131631, 0.0,
    0.468724, -0.824756, 0.316346, 0.0,
    0.829598, 0.43195, 0.353816, 0.0,
    -0.454473, 0.629497, -0.630228, 0.0,
    -0.162349, -0.869962, -0.465628, 0.0,
    0.932805, 0.253451, 0.256198, 0.0,
    -0.345419, 0.927299, -0.144227, 0.0,
    -0.715026, -0.293698, -0.634413, 0.0,
    -0.245997, 0.717467, -0.651711, 0.0,
    -0.967409, -0.250435, -0.037451, 0.0,
    0.901729, 0.397108, -0.170852, 0.0,
    0.892657, -0.0720622, -0.444938, 0.0,
    0.0260084, -0.0361701, 0.999007, 0.0,
    0.949107, -0.19486, 0.247439, 0.0,
    0.471803, -0.807064, -0.355036, 0.0,
    0.879737, 0.141845, 0.453809, 0.0,
    0.570747, 0.696415, 0.435033, 0.0,
    -0.141751, -0.988233, -0.0574584, 0.0,
    -0.58219, -0.0303005, 0.812488, 0.0,
    -0.60922, 0.239482, -0.755975, 0.0,
    0.299394, -0.197066, -0.933557, 0.0,
    -0.851615, -0.220702, -0.47544, 0.0,
    0.848886, 0.341829, -0.403169, 0.0,
    -0.156129, -0.687241, 0.709453, 0.0,
    -0.665651, 0.626724, 0.405124, 0.0,
    0.595914, -0.674582, 0.43569, 0.0,
    0.171025, -0.509292, 0.843428, 0.0,
    0.78605, 0.536414, -0.307222, 0.0,
    0.18905, -0.791613, 0.581042, 0.0,
    -0.294916, 0.844994, 0.446105, 0.0,
    0.342031, -0.58736, -0.7335, 0.0,
    0.57155, 0.7869, 0.232635, 0.0,
    0.885026, -0.408223, 0.223791, 0.0,
    -0.789518, 0.571645, 0.223347, 0.0,
    0.774571, 0.31566, 0.548087, 0.0,
    -0.79695, -0.0433603, -0.602487, 0.0,
    -0.142425, -0.473249, -0.869339, 0.0,
    -0.0698838, 0.170442, 0.982886, 0.0,
    0.687815, -0.484748, 0.540306, 0.0,
    0.543703, -0.534446, -0.647112, 0.0,
    0.97186, 0.184391, -0.146588, 0.0,
    0.707084, 0.485713, -0.513921, 0.0,
    0.942302, 0.331945, 0.043348, 0.0,
    0.499084, 0.599922, 0.625307, 0.0,
    -0.289203, 0.211107, 0.9337, 0.0,
    0.412433, -0.71667, -0.56239, 0.0,
    0.87721, -0.082816, 0.47291, 0.0,
    -0.420685, -0.214278, 0.881538, 0.0,
    0.752558, -0.0391579, 0.657361, 0.0,
    0.0765725, -0.996789, 0.0234082, 0.0,
    -0.544312, -0.309435, -0.779727, 0.0,
    -0.455358, -0.415572, 0.787368, 0.0,
    -0.874586, 0.483746, 0.0330131, 0.0,
    0.245172, -0.0838623, 0.965846, 0.0,
    0.382293, -0.432813, 0.81641, 0.0,
    -0.287735, -0.905514, 0.311853, 0.0,
    -0.667704, 0.704955, -0.239186, 0.0,
    0.717885, -0.464002, -0.518983, 0.0,
    0.976342, -0.214895, 0.0240053, 0.0,
    -0.0733096, -0.921136, 0.382276, 0.0,
    -0.986284, 0.151224, -0.0661379, 0.0,
    -0.899319, -0.429671, 0.0812908, 0.0,
    0.652102, -0.724625, 0.222893, 0.0,
    0.203761, 0.458023, -0.865272, 0.0,
    -0.030396, 0.698724, -0.714745, 0.0,
    -0.460232, 0.839138, 0.289887, 0.0,
    -0.0898602, 0.837894, 0.538386, 0.0,
    -0.731595, 0.0793784, 0.677102, 0.0,
    -0.447236, -0.788397, 0.422386, 0.0,
    0.186481, 0.645855, -0.740335, 0.0,
    -0.259006, 0.935463, 0.240467, 0.0,
    0.445839, 0.819655, -0.359712, 0.0,
    0.349962, 0.755022, -0.554499, 0.0,
    -0.997078, -0.0359577, 0.0673977, 0.0,
    -0.431163, -0.147516, -0.890133, 0.0,
    0.299648, -0.63914, 0.708316, 0.0,
    0.397043, 0.566526, -0.722084, 0.0,
    -0.502489, 0.438308, -0.745246, 0.0,
    0.0687235, 0.354097, 0.93268, 0.0,
    -0.0476651, -0.462597, 0.885286, 0.0,
    -0.221934, 0.900739, -0.373383, 0.0,
    -0.956107, -0.225676, 0.186893, 0.0,
    -0.187627, 0.391487, -0.900852, 0.0,
    -0.224209, -0.315405, 0.92209, 0.0,
    -0.730807, -0.537068, 0.421283, 0.0,
    -0.0353135, -0.816748, 0.575913, 0.0,
    -0.941391, 0.176991, -0.287153, 0.0,
    -0.154174, 0.390458, 0.90762, 0.0,
    -0.283847, 0.533842, 0.796519, 0.0,
    -0.482737, -0.850448, 0.209052, 0.0,
    -0.649175, 0.477748, 0.591886, 0.0,
    0.885373, -0.405387, -0.227543, 0.0,
    -0.147261, 0.181623, -0.972279, 0.0,
    0.0959236, -0.115847, -0.988624, 0.0,
    -0.89724, -0.191348, 0.397928, 0.0,
    0.903553, -0.428461, -0.00350461, 0.0,
    0.849072, -0.295807, -0.437693, 0.0,
    0.65551, 0.741754, -0.141804, 0.0,
    0.61598, -0.178669, 0.767232, 0.0,
    0.0112967, 0.932256, -0.361623, 0.0,
    -0.793031, 0.258012, 0.551845, 0.0,
    0.421933, 0.454311, 0.784585, 0.0,
    -0.319993, 0.0401618, -0.946568, 0.0,
    -0.81571, 0.551307, -0.175151, 0.0,
    -0.377644, 0.00322313, 0.925945, 0.0,
    0.129759, -0.666581, -0.734052, 0.0,
    0.601901, -0.654237, -0.457919, 0.0,
    -0.927463, -0.0343576, -0.372334, 0.0,
    -0.438663, -0.868301, -0.231578, 0.0,
    -0.648845, -0.749138, -0.133387, 0.0,
    0.507393, -0.588294, 0.629653, 0.0,
    0.726958, 0.623665, 0.287358, 0.0,
    0.411159, 0.367614, -0.834151, 0.0,
    0.806333, 0.585117, -0.0864016, 0.0,
    0.263935, -0.880876, 0.392932, 0.0,
    0.421546, -0.201336, 0.884174, 0.0,
    -0.683198, -0.569557, -0.456996, 0.0,
    -0.117116, -0.0406654, -0.992285, 0.0,
    -0.643679, -0.109196, -0.757465, 0.0,
    -0.561559, -0.62989, 0.536554, 0.0,
    0.0628422, 0.104677, -0.992519, 0.0,
    0.480759, -0.2867, -0.828658, 0.0,
    -0.228559, -0.228965, -0.946222, 0.0,
    -0.10194, -0.65706, -0.746914, 0.0,
    0.0689193, -0.678236, 0.731605, 0.0,
    0.401019, -0.754026, 0.52022, 0.0,
    -0.742141, 0.547083, -0.387203, 0.0,
    -0.00210603, -0.796417, -0.604745, 0.0,
    0.296725, -0.409909, -0.862513, 0.0,
    -0.260932, -0.798201, 0.542945, 0.0,
    -0.641628, 0.742379, 0.192838, 0.0,
    -0.186009, -0.101514, 0.97729, 0.0,
    0.106711, -0.962067, 0.251079, 0.0,
    -0.743499, 0.30988, -0.592607, 0.0,
    -0.795853, -0.605066, -0.0226607, 0.0,
    -0.828661, -0.419471, -0.370628, 0.0,
    0.0847218, -0.489815, -0.8677, 0.0,
    -0.381405, 0.788019, -0.483276, 0.0,
    0.282042, -0.953394, 0.107205, 0.0,
    0.530774, 0.847413, 0.0130696, 0.0,
    0.0515397, 0.922524, 0.382484, 0.0,
    -0.631467, -0.709046, 0.313852, 0.0,
    0.688248, 0.517273, 0.508668, 0.0,
    0.646689, -0.333782, -0.685845, 0.0,
    -0.932528, -0.247532, -0.262906, 0.0,
    0.630609, 0.68757, -0.359973, 0.0,
    0.577805, -0.394189, 0.714673, 0.0,
    -0.887833, -0.437301, -0.14325, 0.0,
    0.690982, 0.174003, 0.701617, 0.0,
    -0.866701, 0.0118182, 0.498689, 0.0,
    -0.482876, 0.727143, 0.487949, 0.0,
    -0.577567, 0.682593, -0.447752, 0.0,
    0.373768, 0.0982991, 0.922299, 0.0,
    0.170744, 0.964243, -0.202687, 0.0,
    0.993654, -0.035791, -0.106632, 0.0,
    0.587065, 0.4143, -0.695493, 0.0,
    -0.396509, 0.26509, -0.878924, 0.0,
    -0.0866853, 0.83553, -0.542563, 0.0,
    0.923193, 0.133398, -0.360443, 0.0,
    0.00379108, -0.258618, 0.965972, 0.0,
    0.239144, 0.245154, -0.939526, 0.0,
    0.758731, -0.555871, 0.33961, 0.0,
    0.295355, 0.309513, 0.903862, 0.0,
    0.0531222, -0.91003, -0.411124, 0.0,
    0.270452, 0.0229439, -0.96246, 0.0,
    0.563634, 0.0324352, 0.825387, 0.0,
    0.156326, 0.147392, 0.976646, 0.0,
    -0.0410141, 0.981824, 0.185309, 0.0,
    -0.385562, -0.576343, -0.720535, 0.0,
    0.388281, 0.904441, 0.176702, 0.0,
    0.945561, -0.192859, -0.262146, 0.0,
    0.844504, 0.520193, 0.127325, 0.0,
    0.0330893, 0.999121, -0.0257505, 0.0,
    -0.592616, -0.482475, -0.644999, 0.0,
    0.539471, 0.631024, -0.557476, 0.0,
    0.655851, -0.027319, -0.754396, 0.0,
    0.274465, 0.887659, 0.369772, 0.0,
    -0.123419, 0.975177, -0.183842, 0.0,
    -0.223429, 0.708045, 0.66989, 0.0,
    -0.908654, 0.196302, 0.368528, 0.0,
    -0.95759, -0.00863708, 0.288005, 0.0,
    0.960535, 0.030592, 0.276472, 0.0,
    -0.413146, 0.907537, 0.0754161, 0.0,
    -0.847992, 0.350849, -0.397259, 0.0,
    0.614736, 0.395841, 0.68221, 0.0,
    -0.503504, -0.666128, -0.550234, 0.0,
    -0.268833, -0.738524, -0.618314, 0.0,
    0.792737, -0.60001, -0.107502, 0.0,
    -0.637582, 0.508144, -0.579032, 0.0,
    0.750105, 0.282165, -0.598101, 0.0,
    -0.351199, -0.392294, -0.850155, 0.0,
    0.250126, -0.960993, -0.118025, 0.0,
    -0.732341, 0.680909, -0.0063274, 0.0,
    -0.760674, -0.141009, 0.633634, 0.0,
    0.222823, -0.304012, 0.926243, 0.0,
    0.209178, 0.505671, 0.836984, 0.0,
    0.757914, -0.56629, -0.323857, 0.0,
    -0.782926, -0.339196, 0.52151, 0.0,
    -0.462952, 0.585565, 0.665424, 0.0,
    0.61879, 0.194119, -0.761194, 0.0,
    0.741388, -0.276743, 0.611357, 0.0,
    0.707571, 0.702621, 0.0752872, 0.0,
    0.156562, 0.819977, 0.550569, 0.0,
    -0.793606, 0.440216, 0.42, 0.0,
    0.234547, 0.885309, -0.401517, 0.0,
    0.132598, 0.80115, -0.58359, 0.0,
    -0.377899, -0.639179, 0.669808, 0.0,
    -0.865993, -0.396465, 0.304748, 0.0,
    -0.624815, -0.44283, 0.643046, 0.0,
    -0.485705, 0.825614, -0.287146, 0.0,
    -0.971788, 0.175535, 0.157529, 0.0,
    -0.456027, 0.392629, 0.798675, 0.0,
    -0.0104443, 0.521623, -0.853112, 0.0,
    -0.660575, -0.74519, 0.091282, 0.0,
    -0.0157698, -0.307475, -0.951425, 0.0,
    -0.603467, -0.250192, 0.757121, 0.0,
    0.506876, 0.25006, 0.824952, 0.0,
    0.255404, 0.966794, 0.00884498, 0.0,
    0.466764, -0.874228, -0.133625, 0.0,
    0.475077, -0.0682351, -0.877295, 0.0,
    -0.224967, -0.938972, -0.260233, 0.0,
    -0.377929, -0.814757, -0.439705, 0.0,
    -0.305847, 0.542333, -0.782517, 0.0,
    0.26658, -0.902905, -0.337191, 0.0,
    0.0275773, 0.322158, -0.946284, 0.0,
    0.0185422, 0.716349, 0.697496, 0.0,
    -0.20483, 0.978416, 0.0273371, 0.0,
    -0.898276, 0.373969, 0.230752, 0.0,
    -0.00909378, 0.546594, 0.837349, 0.0,
    0.6602, -0.751089, 0.000959236, 0.0,
    0.855301, -0.303056, 0.420259, 0.0,
    0.797138, 0.0623013, -0.600574, 0.0,
    0.48947, -0.866813, 0.0951509, 0.0,
    0.251142, 0.674531, 0.694216, 0.0,
    -0.578422, -0.737373, -0.348867, 0.0,
    -0.254689, -0.514807, 0.818601, 0.0,
    0.374972, 0.761612, 0.528529, 0.0,
    0.640303, -0.734271, -0.225517, 0.0,
    -0.638076, 0.285527, 0.715075, 0.0,
    0.772956, -0.15984, -0.613995, 0.0,
    0.798217, -0.590628, 0.118356, 0.0,
    -0.986276, -0.0578337, -0.154644, 0.0,
    -0.312988, -0.94549, 0.0899272, 0.0,
    -0.497338, 0.178325, 0.849032, 0.0,
    -0.101136, -0.981014, 0.165477, 0.0,
    -0.521688, 0.0553434, -0.851339, 0.0,
    -0.786182, -0.583814, 0.202678, 0.0,
    -0.565191, 0.821858, -0.0714658, 0.0,
    0.437895, 0.152598, -0.885981, 0.0,
    -0.92394, 0.353436, -0.14635, 0.0,
    0.212189, -0.815162, -0.538969, 0.0,
    -0.859262, 0.143405, -0.491024, 0.0,
    0.991353, 0.112814, 0.0670273, 0.0,
    0.0337884, -0.979891, -0.196654, 0.0
  };


	public void plantTrees() {
		ArrayList<Position> treeList = new ArrayList<Position>();
		for(int x = 2; x < m_width - 3; x++) {
			for(int y = 2; y < m_height - 3; y++) {
				boolean tooClose = false;
				for(Position p : treeList) {
					double distance = Math.sqrt(Math.pow(p.getX()-x,2)+Math.pow(p.getY()-y,2));
					if (distance < 30)
						tooClose = true;
				}
				if (!tooClose) {
					if (m_random.nextInt(100) <= 5) {
						for(int z = m_depth-8; z > 0; z--) {
							if ((m_blocks[x][y][z] == BlockConstants.DIRT || m_blocks[x][y][z] == m_grassBlock) && m_blocks[x][y][z+1] == BlockConstants.AIR) {
								plantTree(x, y, z);
								treeList.add(new Position(x, y, z));
								break;
							} else if (z < m_depth-1 && m_blocks[x][y][z+1] != BlockConstants.AIR) {
								break;
							}
						}
					}
				}
			}
		}
	}

	public void plantTree(int rootX, int rootY, int rootZ) {
		// Place Leaves
		int top = rootZ + 4;
		// Top Layer
		m_blocks[rootX][rootY][top+2] = m_leavesBlock;
		m_blocks[rootX-1][rootY][top+2] = m_leavesBlock;
		m_blocks[rootX+1][rootY][top+2] = m_leavesBlock;
		m_blocks[rootX][rootY-1][top+2] = m_leavesBlock;
		m_blocks[rootX][rootY+1][top+2] = m_leavesBlock;
		// Mid Top Layer
		for (int x = rootX - 1; x < rootX + 2; x++) {
			for (int y = rootY - 1; y < rootY + 2; y++) {
				m_blocks[x][y][top+1] = m_leavesBlock;
			}
		}
		// Bottom layers
		for (int x = rootX - 2; x < rootX + 3; x++) {
			for (int y = rootY - 2; y < rootY + 3; y++) {
				m_blocks[x][y][top] = m_leavesBlock;
				m_blocks[x][y][top-1] = m_leavesBlock;
			}
		}
		// Mid Bottom Layer - Leaf Removal
		m_blocks[rootX-2][rootY-2][top] = BlockConstants.AIR;
		m_blocks[rootX-2][rootY+2][top] = BlockConstants.AIR;
		m_blocks[rootX+2][rootY-2][top] = BlockConstants.AIR;
		m_blocks[rootX+2][rootY+2][top] = BlockConstants.AIR;

		// This is last because the leaves are placed over top areas where there should be a trunk
		// Make the trunk
		for(int z = rootZ;z < rootZ + 5;z++) {
			m_blocks[rootX][rootY][z] = BlockConstants.TREE_TRUNK;
		}
	}

	public void generateCaverns(int count) {
		for (int i = 0; i < count;i++) {
			int x = m_random.nextInt(m_width);
			int y = m_random.nextInt(m_height);
			int z = m_random.nextInt(m_depth/4);
			int radius = m_random.nextInt(60)+40;
			radius = 6;
			int type = m_random.nextInt(100);
			if (type > 90)
				type = BlockConstants.LAVA;
			else if (type > 45)
				type = BlockConstants.AIR;
			else
				type = BlockConstants.WATER;
			for (int m = 0;m < 2; m++) {
				BUBBLE_GEN: for(int j = x-radius;j<x+radius*2;j++) {
					if (j < 0)
						j = 0;
					if (j >= m_width)
						break BUBBLE_GEN;
					for(int k = y-radius;k<y+radius*2;k++) {
						if (k < 0)
							k = 0;
						if (k >= m_height)
							break BUBBLE_GEN;
						for (int l = z-radius;l<z+radius;l++) {
							if (l < 0)
								l = 0;
							if (l >= m_depth)
								break BUBBLE_GEN;
							double distance = Math.sqrt(Math.pow(j-x, 2)+Math.pow(k-y, 2)+Math.pow(l-z, 2));
							if (Math.abs(distance/radius) <= Math.abs(m_random.nextGaussian())) {
								m_blocks[j][k][l] = (byte) type;
							}
						}
					}
				}
				x++;
			}
		}
	}

	public void raiseTerrain() {
		boolean[][][] prevContour = new boolean[m_width][m_height][m_depth];
		boolean[][][] curContour = new boolean[m_width][m_height][m_depth];
		for(int x = 0;x<m_width;x++) {
			for(int y = 0;y<m_height;y++) {
				for(int z = 0;z<m_depth;z++) {
					curContour[x][y][z] = (m_random.nextInt(100) <= 45);
				}
			}
		}
		for(int count = 0;count<3;count++) {
			System.arraycopy(curContour, 0, prevContour, 0, curContour.length);
			for(int x = 0;x<m_width;x++) {
				for(int y = 0;y<m_height;y++) {
					for(int z = 0;z<m_depth;z++) {
						curContour[x][y][z] = simulateCell(prevContour, x, y, z);
					}
				}
			}
		}
		for(int x = 0;x<m_width;x++) {
			for(int y = 0;y<m_height;y++) {
				for(int z = 0;z<m_depth;z++) {
					byte type = (byte)BlockConstants.AIR;
					if (curContour[x][y][m_depth-1-z]) {
						if (z > 1) {
							if (curContour[x][y][m_depth-z-2])
								type = (byte)BlockConstants.DIRT;
							else
								type = BlockConstants.GRASS;
						} else {
							type =(byte)BlockConstants.DIRT;
						}
					}
					m_blocks[x][y][z] = type;
				}
			}
		}
	}

	private boolean simulateCell(boolean[][][] grid, int x, int y, int z) {
		double count = 0;
		boolean isAlive = grid[x][y][z];
		if (isAlive)
			count++;
		if (x > 0 && x < m_width-1) {
			if (grid[x-1][y][z])
				count++;
			if (grid[x+1][y][z])
				count++;
		}

		if (y > 0 && y < m_height-1) {
			if (grid[x][y+1][z])
				count++;
			if (grid[x][y-1][z])
				count++;
		}

		if (z > 0 && z < m_depth-1) {
			if (grid[x][y][z-1])
				count+=2;
			if (grid[x][y][z+1])
				count+=0.9;
		}

		if (y > 0) {
			if (x > 0) {
				if (z > 0)
					if (grid[x-1][y-1][z-1])
						count+=0.7;
				if (z < m_depth-1)
					if (grid[x-1][y-1][z+1])
						count+=0.5;
			}
			if (x < m_width-1) {
				if (z > 0)
					if (grid[x+1][y-1][z-1])
						count+=0.7;
				if (z < m_depth-1)
					if (grid[x+1][y-1][z+1])
						count+=0.5;
			}
		}
		if (y < m_height-1) {
			if (x > 0) {
				if (z > 0)
					if (grid[x-1][y+1][z-1])
						count+=0.7;
				if (z < m_depth-1)
					if (grid[x-1][y+1][z+1])
						count+=0.5;
			}
			if (x < m_width-1) {
				if (z > 0)
					if (grid[x+1][y+1][z-1])
						count+=0.7;
				if (z < m_depth-1)
					if (grid[x+1][y+1][z+1])
						count+=0.5;
			}
		}
		return count >= 5.7;
	}

	/*public void applyContour() {
		int maxHeight = 1;
		for (int x = 0;x<m_width;x++) {
			for (int y = 0;y < m_height;y++) {
				if (m_contour[x][y] > maxHeight)
					maxHeight = m_contour[x][y];
			}
		}
		m_logger.debug("Applying contour");
		for(int x = 0; x < m_width; x++) {
			for(int y = 0; y < m_height; y++) {
				int h = Math.max(0, Math.min(m_depth-1, (m_depth/2) + m_contour[x][y]));
				int d = m_random.nextInt(8) - 4;
				for(int z = 0; z < m_depth; z++) {
					int type = BlockConstants.AIR;
					if (z >= h && z < m_depth/2-1) {
						type = BlockConstants.WATER;
					} else if (z >= h) {
						type = BlockConstants.AIR;
					} else if(z == (h - 1)) {
						type = BlockConstants.GRASS;
					} else if(z < (h - 1) && z > (h -5 )) {
						type = BlockConstants.DIRT;
					} else if(z <= (h - 5 )) {
						type = BlockConstants.ROCK;
					}
					m_blocks[x][y][z] = (byte) type;
				}
			}
		}
	}*/

	public void buildLavaBed(int depth) {
		m_logger.debug("Building lava bed.");
		for (int z = 0;z < depth; z++) {
			for(int x = 0;x < m_width; x++) {
				for (int y = 0; y < m_height; y++ ) {
					m_blocks[x][y][z] = (byte) BlockConstants.LAVA;
				}
			}
		}
	}


	public void simulateOceanFlood() {
		if (m_width < 2 || m_height < 2) { return; }

		LinkedList<Point> toFlood = new LinkedList<Point>();
		int oceanLevel = m_depth / 2 - 1;

		for (int x = 0; x < m_width; x++) {
			if (m_blocks[x][0][oceanLevel] == BlockConstants.AIR) {
				floodBlock(x, 0, oceanLevel);
				if (m_blocks[x][1][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point(x, 1));
				}			}
			if (m_blocks[x][m_height - 1][oceanLevel] == BlockConstants.AIR) {
				floodBlock(x, m_height - 1, oceanLevel);
				if (m_blocks[x][m_height - 2][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point(x, m_height - 2));
				}
			}

		}

		processFlood(toFlood);

		for (int y = 0; y < m_height; y++) {
			if (m_blocks[0][y][oceanLevel] == BlockConstants.AIR) {
				floodBlock(0, y, oceanLevel);
				if (m_blocks[1][y][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point(1, y));
				}
			}
			if (m_blocks[m_width - 1][y][oceanLevel] == BlockConstants.AIR) {
				floodBlock(m_width - 1, y, oceanLevel);
				if (m_blocks[m_width - 2][y][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point(m_width - 2, y));
				}

			}
		}

		processFlood(toFlood);

	}

	private void processFlood(LinkedList<Point> toFlood) {
		int oceanLevel = m_depth / 2 - 1;
		while (toFlood.size() > 0) {
			Point p = toFlood.removeFirst();
			if (m_blocks[(int)(p.getX())][(int)(p.getY())][oceanLevel] != BlockConstants.WATER) {

				floodBlock((int)(p.getX()), (int)(p.getY()), oceanLevel);

				if (p.getX() > 0 && m_blocks[(int)(p.getX() - 1)][(int)(p.getY())][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point((int)(p.getX() - 1), (int)(p.getY())));
				}
				
				if (p.getX() < m_width -1 && m_blocks[(int)(p.getX() + 1)][(int)(p.getY())][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point((int)(p.getX() + 1), (int)(p.getY())));
				}
				
				if (p.getY() > 0 && m_blocks[(int)(p.getX())][(int)(p.getY() - 1)][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point((int)(p.getX()), (int)(p.getY() - 1)));
				}
				
				if (p.getY() < m_height -1 && m_blocks[(int)(p.getX())][(int)(p.getY() + 1)][oceanLevel] == BlockConstants.AIR) {
					toFlood.add(new Point((int)(p.getX()), (int)(p.getY() + 1)));
				}
			}
		}
	}

	// Needs tweaking
	private void createBeach(int x, int y, int z) {
		if (x >= m_width || x < 0 || y >= m_height || y < 0)
			return;
		if (m_blocks[x][y][z] == BlockConstants.GRASS) {
			m_blocks[x][y][z] = BlockConstants.SAND;
			createBeach(x+1, y, z);
			createBeach(x-1, y, z);
			createBeach(x, y+1, z);
			createBeach(x, y-1, z);
		}
	}

	private void floodBlock(int x, int y, int oceanLevel) {
		if (m_blocks[x][y][oceanLevel] == BlockConstants.WATER) {
			return;
		}
		createBeach(x+1, y, oceanLevel);
		createBeach(x-1, y, oceanLevel);
		createBeach(x, y+1, oceanLevel);
		createBeach(x, y-1, oceanLevel);
		for (int z = oceanLevel; true; z--) {
			if (z < 0) { break; }
			if (m_blocks[x][y][z] == BlockConstants.AIR) {
				m_blocks[x][y][z] = BlockConstants.WATER;
			} else if (m_blocks[x][y][z + 1] == BlockConstants.WATER && (m_blocks[x][y][z] == BlockConstants.DIRT || m_blocks[x][y][z] == BlockConstants.GRASS)) {
				m_blocks[x][y][z] = BlockConstants.SAND;
				if (m_blocks[x][y][z-1] == BlockConstants.DIRT || m_blocks[x][y][z-1] == BlockConstants.GRASS) {
					m_blocks[x][y][z-1] = BlockConstants.SAND;
				}
				break;
			}
		}
	}

	protected void makeSnow() {
		for(int x = 0;x<m_width;x++) {
			for(int y = 0;y<m_height;y++) {
				for(int z = 0;z<m_depth;z++) {
					if (m_blocks[x][y][z] == BlockConstants.GRASS) {
						m_blocks[x][y][z] = BlockConstants.CLOTH_WHITE;
						if (m_blocks[x][y][z - 1] == BlockConstants.DIRT) {
							m_blocks[x][y][z - 1] = BlockConstants.CLOTH_WHITE;
						}
					}
					if (z == m_depth/2 - 1 && m_blocks[x][y][z] == BlockConstants.SAND) {
						m_blocks[x][y][z] = BlockConstants.CLOTH_WHITE;
					}
					if (z < m_depth/2 - 1 && m_blocks[x][y][z] == BlockConstants.SAND) {
						m_blocks[x][y][z] = BlockConstants.DIRT;
					}
				}
			}
		}
	}

	protected void makeSand() {
		for(int x = 0;x<m_width;x++) {
			for(int y = 0;y<m_height;y++) {
				for(int z = 0;z<m_depth;z++) {
					if (m_blocks[x][y][z] == BlockConstants.GRASS) {
						m_blocks[x][y][z] = BlockConstants.SAND;
						if (m_blocks[x][y][z - 1] == BlockConstants.DIRT) {
							m_blocks[x][y][z - 1] = BlockConstants.SAND;
						}
					}
				}
			}
		}
	}
}


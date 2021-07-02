package com.dfsek.terra.addons.noise;

import com.dfsek.terra.addons.noise.config.NoiseSamplerBuilderLoader;
import com.dfsek.terra.addons.noise.config.templates.DomainWarpTemplate;
import com.dfsek.terra.addons.noise.config.templates.ImageSamplerTemplate;
import com.dfsek.terra.addons.noise.config.templates.KernelTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.CellularNoiseTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.ConstantNoiseTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.ExpressionFunctionTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.GaborNoiseTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.SimpleNoiseTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.fractal.BrownianMotionTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.fractal.PingPongTemplate;
import com.dfsek.terra.addons.noise.config.templates.noise.fractal.RidgedFractalTemplate;
import com.dfsek.terra.addons.noise.config.templates.normalizer.ClampNormalizerTemplate;
import com.dfsek.terra.addons.noise.config.templates.normalizer.LinearNormalizerTemplate;
import com.dfsek.terra.addons.noise.config.templates.normalizer.NormalNormalizerTemplate;
import com.dfsek.terra.addons.noise.samplers.ImageSampler;
import com.dfsek.terra.addons.noise.samplers.noise.CellularSampler;
import com.dfsek.terra.addons.noise.samplers.noise.random.GaussianNoiseSampler;
import com.dfsek.terra.addons.noise.samplers.noise.random.WhiteNoiseSampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.OpenSimplex2SSampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.PerlinSampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.SimplexSampler;
import com.dfsek.terra.addons.noise.samplers.noise.value.ValueCubicSampler;
import com.dfsek.terra.addons.noise.samplers.noise.value.ValueSampler;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.annotations.Global;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.seeded.NoiseProvider;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

@Addon("noise")
@Author("Terra")
@Version("1.0.0")
public class NoiseAddon extends TerraAddon {
    @Inject
    private TerraPlugin plugin;

    @Override
    public void initialize() {
        plugin.applyLoader(ImageSamplerTemplate.class, ImageSamplerTemplate::new)
                .applyLoader(DomainWarpTemplate.class, DomainWarpTemplate::new)
                .applyLoader(LinearNormalizerTemplate.class, LinearNormalizerTemplate::new)
                .applyLoader(NormalNormalizerTemplate.class, NormalNormalizerTemplate::new)
                .applyLoader(ImageSampler.Channel.class, (t, object, cf) -> ImageSampler.Channel.valueOf((String) object))
                .applyLoader(ClampNormalizerTemplate.class, ClampNormalizerTemplate::new)
                .applyLoader(ImageSamplerTemplate.class, ImageSamplerTemplate::new)
                .applyLoader(CellularSampler.ReturnType.class, (t, object, cf) -> CellularSampler.ReturnType.valueOf((String) object))
                .applyLoader(CellularSampler.DistanceFunction.class, (t, object, cf) -> CellularSampler.DistanceFunction.valueOf((String) object));
    }
    
    @SuppressWarnings("deprecation")
    @Global
    public void packPreLoad(ConfigPackPreLoadEvent event) {

        event.getPack()
                .applyLoader(NoiseSeeded.class, new NoiseSamplerBuilderLoader(event.getPack().getRegistry(NoiseProvider.class)));

        CheckedRegistry<NoiseProvider> noiseRegistry = event.getPack().getRegistry(NoiseProvider.class);
        
        noiseRegistry.addUnchecked("LINEAR", LinearNormalizerTemplate::new);
        noiseRegistry.addUnchecked("NORMAL", NormalNormalizerTemplate::new);
        noiseRegistry.addUnchecked("CLAMP", ClampNormalizerTemplate::new);
        noiseRegistry.addUnchecked("EXPRESSION", ExpressionFunctionTemplate::new);

        noiseRegistry.addUnchecked("IMAGE", ImageSamplerTemplate::new);

        noiseRegistry.addUnchecked("DOMAINWARP", DomainWarpTemplate::new);

        noiseRegistry.addUnchecked("FBM", BrownianMotionTemplate::new);
        noiseRegistry.addUnchecked("PINGPONG", PingPongTemplate::new);
        noiseRegistry.addUnchecked("RIDGED", RidgedFractalTemplate::new);

        noiseRegistry.addUnchecked("OPENSIMPLEX2", () -> new SimpleNoiseTemplate(OpenSimplex2Sampler::new));
        noiseRegistry.addUnchecked("OPENSIMPLEX2S", () -> new SimpleNoiseTemplate(OpenSimplex2SSampler::new));
        noiseRegistry.addUnchecked("PERLIN", () -> new SimpleNoiseTemplate(PerlinSampler::new));
        noiseRegistry.addUnchecked("SIMPLEX", () -> new SimpleNoiseTemplate(SimplexSampler::new));
        noiseRegistry.addUnchecked("GABOR", GaborNoiseTemplate::new);


        noiseRegistry.addUnchecked("VALUE", () -> new SimpleNoiseTemplate(ValueSampler::new));
        noiseRegistry.addUnchecked("VALUECUBIC", () -> new SimpleNoiseTemplate(ValueCubicSampler::new));

        noiseRegistry.addUnchecked("CELLULAR", CellularNoiseTemplate::new);

        noiseRegistry.addUnchecked("WHITENOISE", () -> new SimpleNoiseTemplate(WhiteNoiseSampler::new));
        noiseRegistry.addUnchecked("GAUSSIAN", () -> new SimpleNoiseTemplate(GaussianNoiseSampler::new));

        noiseRegistry.addUnchecked("CONSTANT", ConstantNoiseTemplate::new);

        noiseRegistry.addUnchecked("KERNEL", KernelTemplate::new);
    }
}

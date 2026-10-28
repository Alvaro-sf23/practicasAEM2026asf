



package com.minsaitviajes.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

@Model(
    adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TagsModel {



    private static final Logger LOG =
            LoggerFactory.getLogger(TagsModel.class);

    @Self
    private Resource currentResource;

    @SlingObject
    private ResourceResolver resourceResolver;

    private List<String> tagTitles;

    @PostConstruct
    protected void init() {
        tagTitles = new ArrayList<>();

        if (currentResource == null || resourceResolver == null) {
            LOG.warn("TagsModel: no se ha podido obtener el recurso o el ResourceResolver.");
            return;
        }

        ValueMap properties = currentResource.getValueMap();

        String[] tagIds = properties.get("cq:tags", String[].class);

        // Compatibilidad por si cq:tags contiene un único String.
        if (tagIds == null || tagIds.length == 0) {
            String singleTag = properties.get("cq:tags", String.class);

            if (singleTag != null && !singleTag.isEmpty()) {
                tagIds = new String[] { singleTag };
            }
        }

        if (tagIds == null || tagIds.length == 0) {
            LOG.info(
                "TagsModel: no se han encontrado tags en el recurso {}",
                currentResource.getPath()
            );
            return;
        }

        TagManager tagManager =
                resourceResolver.adaptTo(TagManager.class);

        if (tagManager == null) {
            LOG.error("TagsModel: no se ha podido obtener TagManager.");
            return;
        }

        for (String tagId : tagIds) {
            if (tagId == null || tagId.isEmpty()) {
                continue;
            }

            Tag tag = tagManager.resolve(tagId);

            LOG.info(
                "TagsModel - tagId: {}, tag resuelto: {}",
                tagId,
                tag
            );

            if (tag != null) {
                String title = tag.getTitle();

                LOG.info(
                    "TagsModel - título obtenido: {}",
                    title
                );

                if (title != null && !title.isEmpty()) {
                    tagTitles.add(title);
                }
            } else {
                LOG.warn(
                    "TagsModel: no se ha podido resolver el tag {}",
                    tagId
                );
            }
        }
    }

    public List<String> getTagTitles() {
        return Collections.unmodifiableList(tagTitles);
    }
}
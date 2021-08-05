package ru.phoenix.engine.core.buffer.template;

import ru.phoenix.engine.math.variable.Matrix4f;

public class ObjectConfiguration {
    private float[] positions;
    private float[] normals;
    private float[] textureCords;
    private float[] tangents;
    private float[] biTangents;
    private int[] boneIds;
    private float[] boneWeights;
    private Matrix4f[] instances;
    private int[] indices;

    public ObjectConfiguration(float[] positions, float[] normals, float[] textureCords, float[] tangents, float[] biTangents, int[] boneIds, float[] boneWeights, Matrix4f[] instances, int[] indices) {
        setPositions(positions);
        setNormals(normals);
        setTextureCords(textureCords);
        setTangents(tangents);
        setBiTangents(biTangents);
        setBoneIds(boneIds);
        setBoneWeights(boneWeights);
        setInstances(instances);
        setIndices(indices);
    }

    public ObjectConfiguration(float[] positions, float[] textureCords, int[] indices) {
        setPositions(positions);
        setNormals(null);
        setTextureCords(textureCords);
        setTangents(null);
        setBiTangents(null);
        setBoneIds(null);
        setBoneWeights(null);
        setInstances(null);
        setIndices(indices);
    }

    public ObjectConfiguration(ObjectConfiguration objectConfiguration) {
        setPositions(objectConfiguration.getPositions());
        setNormals(objectConfiguration.getNormals());
        setTextureCords(objectConfiguration.getTextureCords());
        setTangents(objectConfiguration.getTangents());
        setBiTangents(objectConfiguration.getBiTangents());
        setBoneIds(objectConfiguration.getBoneIds());
        setBoneWeights(objectConfiguration.getBoneWeights());
        setInstances(objectConfiguration.getInstances());
        setIndices(objectConfiguration.getIndices());
    }

    public float[] getPositions() {
        return positions;
    }

    public void setPositions(float[] positions) {
        this.positions = positions;
    }

    public float[] getNormals() {
        return normals;
    }

    public void setNormals(float[] normals) {
        this.normals = normals;
    }

    public float[] getTextureCords() {
        return textureCords;
    }

    public void setTextureCords(float[] textureCords) {
        this.textureCords = textureCords;
    }

    public float[] getTangents() {
        return tangents;
    }

    public void setTangents(float[] tangents) {
        this.tangents = tangents;
    }

    public float[] getBiTangents() {
        return biTangents;
    }

    public void setBiTangents(float[] biTangents) {
        this.biTangents = biTangents;
    }

    public int[] getBoneIds() {
        return boneIds;
    }

    public void setBoneIds(int[] boneIds) {
        this.boneIds = boneIds;
    }

    public float[] getBoneWeights() {
        return boneWeights;
    }

    public void setBoneWeights(float[] boneWeights) {
        this.boneWeights = boneWeights;
    }

    public Matrix4f[] getInstances() {
        return instances;
    }

    public void setInstances(Matrix4f[] instances) {
        this.instances = instances;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }
}

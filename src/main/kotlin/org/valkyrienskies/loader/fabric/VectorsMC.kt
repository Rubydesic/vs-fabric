package org.valkyrienskies.loader.fabric

import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Quaternion
import org.joml.Matrix4dc
import org.joml.Quaternionf
import org.joml.Vector3d


fun applyTransform(transform: Matrix4dc, stack: MatrixStack) {
    val rot = transform.getNormalizedRotation(Quaternionf())
    val trans = transform.getTranslation(Vector3d())

    stack.multiply(Quaternion(rot.x, rot.y, rot.z, rot.w))
    stack.translate(trans.x, trans.y, trans.z)
}
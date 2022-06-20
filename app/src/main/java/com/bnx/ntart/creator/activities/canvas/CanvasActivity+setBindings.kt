package com.bnx.ntart.creator.activities.canvas

import com.bnx.ntart.databinding.ActivityCanvasBinding

fun CanvasActivity.setBindings() {
    binding = ActivityCanvasBinding.inflate(layoutInflater)
    setContentView(binding.root)
}
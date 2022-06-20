package com.bnx.ntart.creator.fragments.tools

import com.bnx.ntart.creator.listeners.ToolsFragmentListener
import com.bnx.ntart.databinding.ActionFragmentToolsBinding

var binding_: ActionFragmentToolsBinding? = null

val binding get() = binding_!!

lateinit var caller: ToolsFragmentListener
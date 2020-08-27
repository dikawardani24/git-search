package wardani.dika.gitsearch.util

fun <I, O> mapToList(inputs: List<I>, mapper: (input: I) -> O): List<O> {
    val outputs = arrayListOf<O>()
    inputs.forEach {
        val output = mapper(it)
        outputs.add(output)
    }

    return outputs
}
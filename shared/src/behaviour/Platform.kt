package uk.thehunt.sleep
interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
import { useRouter } from "next/router";

const strings = {
    "en-US": {"home.createArticle.button": "Crear"},
    "es-ES": {"home.createArticle.button": "Create cuio"}
}

const I18n = (code) => {
    const { locale } = useRouter();
    return strings[locale][code]
}

export default I18n
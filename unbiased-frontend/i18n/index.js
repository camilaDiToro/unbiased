import { useRouter } from "next/router";

const strings = {
  "en-US": {
    //Home
    "home.createArticle.button": "Create",
    "home.emptyCreators": "There hasn't been any activity yet.",
    "home.topCreators": "Top Creators",
    "search.filter": (params) => `Cancel search filter: \"${params[0]}\"`,
    "search.notFound": (params) =>
      `We could not find any results that match \"${params[0]}\"`,
    "order.top": "Top",
    "order.new": "New",
    "categories.notFound": (params) =>
      `We could not find any results for the category \"${params[0]}\"`,
    "categories.tourism": "Tourism",
    "categories.entertainment": "Entertainment",
    "categories.politics": "Politics",
    "categories.economics": "Economics",
    "categories.sports": "Sports",
    "categories.technology": "Technology",
    "categories.all": "All",
    "categories.forMe": "For me",
    "views.register.title": "Register",
  },
  "es-ES": {
    //Home
    "home.createArticle.button": "Crear",
    "home.emptyCreators": "No ha habido actividad aún.",
    "home.topCreators": "Creadores populares",
    "search.filter": (params) => `Cancelar filtro de búsqueda: \"${params[0]}\"`,
    "search.notFound": (params) =>
        `No se encontraron resultados para la búsqueda \"${params[0]}\"`,
    "order.top": "Más popular",
    "order.new": "Nuevo",
    "categories.notFound": (params) =>
        `No se encontraron resultados para la categoría \"${params[0]}\"`,
    "categories.tourism": "Turismo",
    "categories.entertainment": "Espectáculo",
    "categories.politics": "Política",
    "categories.economics": "Economía",
    "categories.sports": "Deportes",
    "categories.technology": "Tecnología",
    "categories.all": "Todo",
    "categories.forMe": "Para mi",
    "views.register.title": "Registrarse",
  },
};

const I18n = (code, params) => {
  const { locale } = useRouter();
  const maybeFunc = strings[locale][code];
  if (typeof maybeFunc == "function") {
    return maybeFunc(params);
  }

  return maybeFunc;
};

export default I18n;

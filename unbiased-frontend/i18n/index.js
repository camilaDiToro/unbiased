import { useRouter } from "next/router";

const strings = {
  "en-US": {
    //NavBar
    "navbar.search": "Search",
    "navbar.logOut": "Log out",
    "navbar.myProfile": "My profile",
    "navbar.logIn": "Log in",
    "navbar.register": "Sign up",
    "navbar.adminPanel": "Administration panel",

    //Home
    "home.createArticle.button": "Create",
    "home.emptyCreators": "There hasn't been any activity yet.",
    "home.topCreators": "Top Creators",
    "search.filter": (params) => `Cancel search filter: \"${params[0]}\"`,
    "search.notFound": (params) =>
      `We could not find any results that match \"${params[0]}\"`,

    //Categories
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

    //Profile
    "profile.edit": "Edit",
    "profile.modal.question": "Are you sure you want to delete the article?",
    "profile.modal.msg": "Accepting this you will lose the article and you will not be able to recover it again. Are you sure you would like to continue?",
    "profile.modal.accept": "Confirm",
    "profile.user.settings": "User settings",
    "profile.modal.save": "Save",
    "profile.modal.changeUsername": "Username",
    "profile.modal.changeProfilePicture": "Profile picture",
    "profile.modal.changeDescription": "Description",
    "profile.modal.changeMailOptions": "Notify me by email when the following events occur:",
    "profile.follow": "Follow",
    "profile.following": "Following",
    "profile.modal.infoTitle": "Account information",
    "profile.modal.infoAllowedMsg": "You are allowed to:",
    "profile.modal.infoChangeUsername": "Change your username",
    "profile.modal.infoChangeProfileimg": "Change your profile image",
    "profile.modal.infoChangeAddDescription": "Add a description",
    "profile.modal.enabled": "Enabled",
    "profile.modal.disabled": "Desabled",
    "profile.pin.question": "Are you sure you want to pin the article?",
    "profile.pin.body": "Pinning this article it will appear on top of the rest of the news. Are you sure you would like to continue?",
    "profile.unpin.question": "Are you sure you want to unpin the article?",
    "profile.unpin.body": "Unpinning this article it will appear on top of the rest of the news. Are you sure you would like to continue?",
    "profile.followers": "Followers",
  },
  "es-ES": {
    //NavBar
    "navbar.search": "Buscar",
    "navbar.logOut": "Cerrar Sesión",
    "navbar.myProfile": "Mi Perfil",
    "navbar.logIn": "Iniciar Sesión",
    "navbar.register": "Registrarme",
    "navbar.adminPanel": "Panel de Administración",

    //Home
    "home.createArticle.button": "Crear",
    "home.emptyCreators": "No ha habido actividad aún.",
    "home.topCreators": "Creadores populares",
    "search.filter": (params) => `Cancelar filtro de búsqueda: \"${params[0]}\"`,
    "search.notFound": (params) =>
        `No se encontraron resultados para la búsqueda \"${params[0]}\"`,

    //Categories
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

    //Profile
    "profile.edit": "Editar",
    "profile.modal.question": "¿Estás seguro de que quieres eliminar esta noticia?",
    "profile.modal.msg": "Al eliminar de esta noticia no podra volver a recuperarla. ¿EstÃás seguro de que deseas continuar?",
    "profile.modal.accept": "Confirmar",
    "profile.user.settings": "Ajustes de usuario",
    "profile.modal.save": "Guardar",
    "profile.modal.changeUsername": "Nombre de usuario",
    "profile.modal.changeProfilePicture": "Imágen de perfil",
    "profile.modal.changeDescription": "Descripción",
    "profile.modal.changeMailOptions": "Notificarme por correo cuando los siguientes eventos ocurren:",
    "profile.follow": "Seguir",
    "profile.following": "Siguiendo",
    "profile.modal.infoTitle": "Informacion de la cuenta",
    "profile.modal.infoAllowedMsg": "Tenes permitido:",
    "profile.modal.infoChangeUsername": "Cambiar tu nombre de usuario",
    "profile.modal.infoChangeProfileimg": "Cambiar tu imagen de perfil",
    "profile.modal.infoChangeAddDescription": "Añadir una descripción",
    "profile.modal.enabled": "Habilitado",
    "profile.modal.disabled": "Deshabilitado",
    "profile.pin.question": "¿Estás seguro de que quieres fijar esta noticia?",
    "profile.pin.body": "Al fijar esta noticia podrás visualizarla sobre las demas noticias. ¿Estás seguro de que deseas continuar?",
    "profile.unpin.question": "¿EstÃ¡s seguro de que quieres desfijar esta noticia?",
    "profile.unpin.body": "Al desfijar esta noticia no podrás visualizarla sobre las demas noticias. ¿Estás seguro de que deseas continuar?",
    "profile.followers": "Seguidores",
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

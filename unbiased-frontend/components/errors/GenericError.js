import { useAppContext } from "../../context";
import Link from "next/link";

export default function GenericError(props){
  const {I18n, loggedUser}= useAppContext();

  return(
  <>
    <h1 className="display-1 fw-bold">404</h1>
    <p class="fs-1">
      <span class="text-info font-weight-bold">{I18n("genericError.ops")}</span>
      {I18n("genericError.message.404")}
    </p>
    <p class="lead">
      <spring:message code="error.commentNotFound"/>
    </p>
    <button className="btn btn-info" type="submit">{I18n("genericError.button.goHome")}</button>
  </>
  )
}
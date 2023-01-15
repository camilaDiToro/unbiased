import Tooltip from "./Tooltip";
import {useAppContext} from "../context";

export default function UserPrivileges(props) {
    const {loggedUser, I18n} = useAppContext()
    return <>
        <h6>
            {I18n("profile.modal.infoAllowedMsg")}
        </h6>

        <div className="info-function d-flex flex-row mb-3">

            <div className="d-flex">
                1. {I18n("profile.modal.infoChangeUsername")}
            </div>

            <div className="d-flex info-enabled info-custom-box">
                {I18n("profile.modal.enabled")}
            </div>
        </div>

        <div className="info-function d-flex flex-row mb-3">

            <div className="d-flex">
                2. {I18n("profile.modal.infoChangeProfileimg")}
            </div>

            <div className="d-flex info-enabled info-custom-box">
                {I18n("profile.modal.enabled")}
            </div>
        </div>

        <div className="info-function d-flex flex-row">

            <div className="d-flex">
                3. {I18n("profile.modal.infoChangeAddDescription")}
            </div>
            {loggedUser.isJournalist ? <div className="d-flex info-enabled info-custom-box">
                {I18n("profile.modal.enabled")}
            </div> : <Tooltip className="d-flex info-disabled info-custom-box" position="bottom"
                              text={I18n("tooltip.infoDisabled")}>
                {I18n("profile.modal.disabled")}
            </Tooltip>}
        </div></>
}
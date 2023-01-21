import {useState} from "react";
import {useAppContext} from "../context";

export default function EditProfileForm(props) {
    const {I18n, axios} = useAppContext()
    const [settings, setSettings] = useState({
        username: props.username,
        description: props.description,
        mailOptions: props.mailOptions
    });

    const [file, setFile] = useState(new FormData())

    const handleEditFormSubmit = async (e) => {
        const res = await axios.put(`users/${props.id}`, JSON.stringify(settings),{
            headers: {
                'Content-Type': 'application/json',
            }
        })
if (file.has('image')) {
    const fileRes = await axios.put(`users/${props.id}/image`, file)
}

    props.triggerEffect()
    }

    props.handlerArray[0] = handleEditFormSubmit

    const [filename, setFilename] = useState(I18n("createArticle.selectFile"))

    const mailOptions = ["mailOption.follow", "mailOption.comment", "mailOption.folowingPublished", "mailOption.positivityChanged"]

    const handleFileChange = (e) => {
        const el = e.target
        setFile(f => {
            f.set('image', el.files[0])
            return f
        })
        setFilename(el.files[0].name)

    }

    const handleChange = (e) => {
        const el = e.target
        if (el.type === 'checkbox') {
            if (el.checked) {
                const aux = settings.mailOptions
                aux.push(el.value)
                setSettings({...settings, mailOptions: aux})
            } else {
                setSettings({...settings, mailOptions: settings.mailOptions.filter(c => c !== el.value)})
            }
        } else {
            setSettings({...settings, [el.name]:  el.value})
        }

    }

    return <>
        <label >
            {I18n("profile.modal.changeUsername")}
        </label>
        <div className="input-group mb-3">
            <div className="input-group-prepend">
                <span className="input-group-text" id="basic-addon1">@</span>
            </div>
            <input value={settings.username} onChange={handleChange} type="text" className="form-control" id="username-input" name="username"
                   placeholder={I18n("profile.modal.changeUsername")} />
        </div>

        <label >
            {I18n("profile.modal.changeProfilePicture")}
        </label>
        <div className="input-group mb-3">
            <div className="custom-file">
                <input id="file-input" type="file" accept="image/png, image/jpeg"
                       className="custom-file-input" name="image" onChange={handleFileChange}/>
                <label id="file-input-label" className="custom-file-label"
                       htmlFor="inputGroupFile01">{filename}</label>

            </div>

        </div>


        {props.isJournalist ?
            <>
                <label>
                    {I18n("profile.modal.changeDescription")}
                </label>
                <div className="input-group mb-3">
                    <input name="description" onChange={handleChange} type="text" className="form-control" id="description-input"
                           placeholder={I18n("profile.modal.changeDescription")} value={settings.description}/>
                </div>
            </> : <></>}

        <div className="input-group mb-3">
            <label>
                {I18n("profile.modal.changeMailOptions")}
            </label>
            {mailOptions.map(op => <div key={op} className="form-check  w-100">

                <input onChange={handleChange} className="mr-1" checked={!!settings.mailOptions.find(o => o === op)} type="checkbox" value={op} />
                <label className="form-check-label">
                    {I18n(op)}
                </label>
            </div>)}

        </div>
    </>
}
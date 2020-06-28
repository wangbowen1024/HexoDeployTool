# 删除服务器上旧文件
ssh root@domain.com rm -rf /yourHexoPath/source/_posts/*
# 上传新文件
scp /....../HexoUtil/output/_posts/* root@domain.com:/yourHexoPath/source/_posts/
# 执行更新脚本
ssh root@domain.com ./hexoUpdate.sh